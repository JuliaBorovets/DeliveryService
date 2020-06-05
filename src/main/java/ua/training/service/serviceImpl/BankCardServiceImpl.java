package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;
import ua.training.mappers.BankCardMapper;
import ua.training.mappers.DtoToCheckConverter;
import ua.training.repository.BankCardRepository;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.BankCardService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BankCardServiceImpl implements BankCardService {

    private final BankCardRepository bankCardRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderCheckRepository orderCheckRepository;
    private final DtoToCheckConverter dtoToCheckConverter;


    public BankCardServiceImpl(BankCardRepository bankCardRepository, UserService userService,
                               OrderService orderService, OrderCheckRepository orderCheckRepository, DtoToCheckConverter dtoToCheckConverter) {
        this.bankCardRepository = bankCardRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.orderCheckRepository = orderCheckRepository;
        this.dtoToCheckConverter = dtoToCheckConverter;
    }

    private BankCard findById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("can not find bank card with id = " + id));
    }

    @Transactional
    public void deleteBankCard(Long bankId) {

        bankCardRepository.deleteById(bankId);
    }

    @Override
    public List<BankCardDto> getAllUserBankCards(User user) {

        return bankCardRepository
                .findBankCardByUsers(user).stream()
                .map(BankCardMapper.INSTANCE::bankCardToDto).collect(Collectors.toList());

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    @Override
    public void saveBankCardDTO(BankCardDto bankCardDTO, Long userId) throws BankException {
        User user = userService.findUserById(userId);

        Optional<BankCard> optionalBankCard = bankCardRepository.findBankCardByIdAndExpMonthAndExpYearAndCcv(
                        bankCardDTO.getId(), bankCardDTO.getExpMonth(), bankCardDTO.getExpYear(), bankCardDTO.getCcv()
                );

        BankCard bankCardToSave = optionalBankCard
                .orElseGet(() -> BankCardMapper.INSTANCE.bankCardDtoToBankCard(bankCardDTO));

        user.getCards().add(bankCardToSave);

        try {
            bankCardRepository.save(bankCardToSave);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card");
        }

    }


    @Transactional
    @Override
    public void updateBankCardDTO(BankCardDto bankCardDTO) {
        bankCardRepository.save(BankCardMapper.INSTANCE.bankCardDtoToBankCard(bankCardDTO));
    }


    @Transactional
    @Override
    public void payForOrder(OrderCheckDto orderCheckDto) throws OrderNotFoundException, BankException {

        Order order = orderService.findOrderById(orderCheckDto.getOrderId());

        if (order.getStatus().equals(Status.SHIPPED) || order.getStatus().equals(Status.PAID)){
            throw new BankException("order is already paid");
        }

        User user = userService.findUserById(orderCheckDto.getUser().getId());

        BankCard bankCard =  bankCardRepository.findById(orderCheckDto.getBankCard())
                .orElseThrow(()->new RuntimeException("dd"));

        OrderCheck orderCheck = OrderCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .order(order)
                .build();

        processPaying(orderCheck, order);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processPaying(OrderCheck orderCheck, Order order) throws BankException {
        BigDecimal moneyToPay = order.getShippingPriceInCents();
        sendMoney(orderCheck.getBankCard().getId(), 1111L, moneyToPay);
        orderCheck.setPriceInCents(moneyToPay);
        orderCheck.setCreationDate(LocalDate.now());
        order.setCheck(orderCheck);
        orderCheck.getOrder().setStatus(Status.PAID);
        orderCheckRepository.save(orderCheck);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException {
        replenishBankCard(fromAccountId, amount.negate());
        replenishBankCard(toAccountId, amount);

    }

    public void replenishBankCard(Long bankId, BigDecimal moneyToAdd) throws BankException {
        BankCard bankCard = findById(bankId);
        bankCard.setBalance(bankCard.getBalance().add(moneyToAdd));
        bankCardRepository.save(bankCard);
    }

    @Override
    public BankCardDto findBankCardDtoById(Long id) {
        return bankCardRepository
                .findById(id)
                .map(BankCardMapper.INSTANCE::bankCardToDto)
                .orElseThrow(()->new RuntimeException("no card"));
    }

    @Override
    public BankCard findBankCardById(Long id) {
        return bankCardRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("no card"));
    }
}
