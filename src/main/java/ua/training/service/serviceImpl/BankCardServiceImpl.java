package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.converters.BankCardToDtoConverter;
import ua.training.converters.DtoToBankCardConverter;
import ua.training.dto.BankCardDto;
import ua.training.dto.UserDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;
import ua.training.repository.BankCardRepository;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;
import ua.training.service.BankCardService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BankCardServiceImpl implements BankCardService {
    private final BankCardRepository bankCardRepository;

    private final BankCardToDtoConverter bankCardToDtoConverter;
    private final DtoToBankCardConverter dtoToBankCardConverter;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public BankCardServiceImpl(BankCardRepository bankCardRepository, BankCardToDtoConverter bankCardToDtoConverter,
                               DtoToBankCardConverter dtoToBankCardConverter, UserRepository userRepository,
                               OrderRepository orderRepository) {
        this.bankCardRepository = bankCardRepository;
        this.bankCardToDtoConverter = bankCardToDtoConverter;
        this.dtoToBankCardConverter = dtoToBankCardConverter;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    private BankCard findById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("can not find bank card with id = " + id));
    }

    public void deleteBankCard(Long bankId) {
        bankCardRepository.deleteById(bankId);
    }


    public void replenishBankCard(Long bankId, BigDecimal moneyToAdd) throws BankException {
        BankCard bankCard = findById(bankId);
        bankCard.setBalance(bankCard.getBalance().add(moneyToAdd));
        bankCardRepository.save(bankCard);
    }

    @Override
    public List<BankCardDto> getAllUserBankCards(User user) {

        return bankCardRepository
                .findBankCardByUsers(user).stream()
                .map(bankCardToDtoConverter::convert).collect(Collectors.toList());

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    @Override
    public BankCardDto saveBankCardDTO(BankCardDto bankCardDTO, Long userId) throws BankException {
        User user = userRepository.findUserById(userId).orElseThrow(() ->new RuntimeException("can not save bank Card") );

        BankCard bankCard = dtoToBankCardConverter.convert(bankCardDTO);
        user.addBankCard(bankCard);

        try {
            assert bankCard != null;
            bankCardRepository.save(bankCard);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card");
        }

        return bankCardDTO;
    }


    private Long getAdminAccount(){
        return 1L;
    }

    @Override
    public Long payForOrder(Long orderId, Long bankCardId, UserDto userDto) throws BankException, OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("can not find order"));

        if (order.getStatus().equals(Status.SHIPPED) || order.getStatus().equals(Status.PAID)){
            throw new BankException("order is already paid");
        }

        BankCard bankCard = bankCardRepository
                .findById(bankCardId)
                .orElseThrow(()-> new BankException("can not find bank card"));

        User user = userRepository.findUserById(userDto.getId()).orElseThrow(()->new RuntimeException("can not find user"));

        OrderCheck orderCheck = createCheck(order, bankCard, user);

        return orderCheck.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public OrderCheck createCheck(Order order, BankCard bankCard, User user) throws BankException {
        OrderCheck orderCheck = new OrderCheck();

        BigDecimal moneyToPay = order.getShippingPriceInCents();
        sendMoney(user.getId(), getAdminAccount(), moneyToPay);

        orderCheck.create(order, bankCard, user);

        return orderCheck;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException {
        replenishBankCard(fromAccountId, amount.negate());
        replenishBankCard(toAccountId, amount);

    }

}
