package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.CanNotPayException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;
import ua.training.mappers.BankCardMapper;
import ua.training.repository.BankCardRepository;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.BankCardService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class BankCardServiceImpl implements BankCardService {

    private final BankCardRepository bankCardRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderCheckRepository orderCheckRepository;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.id}")
    private Long ACCOUNT_TO_SEND_MONEY_ID;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.expMonth}")
    private Long ACCOUNT_TO_SEND_MONEY_EXP_MONTH;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.expYear}")
    private Long ACCOUNT_TO_SEND_MONEY_EXP_YEAR;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.ccv}")
    private Long ACCOUNT_TO_SEND_MONEY_CCV;

    public BankCardServiceImpl(BankCardRepository bankCardRepository, UserService userService,
                               OrderService orderService, OrderCheckRepository orderCheckRepository) {
        this.bankCardRepository = bankCardRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.orderCheckRepository = orderCheckRepository;
    }

    private BankCard findById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("can not find bank card with id = " + id));
    }

    @Transactional
    public void deleteBankCardConnectionWithUser(Long bankId, Long userId) throws BankException {

        BankCard  bankCard = findBankCardById(bankId);

        User user = userService.findUserById(userId);

        bankCard.deleteUser(user);

    }

    @Override
    public List<BankCardDto> getAllUserBankCards(User user) {

        return bankCardRepository
                .findBankCardByUsers(user).stream()
                .map(BankCardMapper.INSTANCE::bankCardToDto)
                .collect(Collectors.toList());

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
            throw new BankException("Can not save bank card with  id=" + bankCardDTO.getId());
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    @PostConstruct
    public void createAccountToSendMoney() throws BankException {
        BankCard bankCard = BankCard.builder()
                .id(ACCOUNT_TO_SEND_MONEY_ID)
                .expMonth(ACCOUNT_TO_SEND_MONEY_EXP_MONTH)
                .expYear(ACCOUNT_TO_SEND_MONEY_EXP_YEAR)
                .balance(BigDecimal.ZERO)
                .ccv(ACCOUNT_TO_SEND_MONEY_CCV).build();
        try {
            bankCardRepository.save(bankCard);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card with  id=" + bankCard.getId());
        }
    }


    @Override
    public void updateBankCardDTO(BankCardDto bankCardDTO, Long bankCardId) throws BankException {
        BankCard bankCard = findBankCardById(bankCardId);
        bankCard.setBalance(bankCardDTO.getBalance());
        bankCardRepository.save(bankCard);
    }


    @Override
    public void payForOrder(OrderCheckDto orderCheckDto) throws OrderNotFoundException, BankException, CanNotPayException {

        Order order = orderService.findOrderById(orderCheckDto.getOrderId());

        if (order.getStatus().equals(Status.SHIPPED) || order.getStatus().equals(Status.PAID)){
            throw new BankException("order with  id=" + order.getId() + " is already paid");
        }

        BankCard bankCard =  bankCardRepository.findById(orderCheckDto.getBankCard())
                .orElseThrow(()->new BankException("no bank card with id=" + orderCheckDto.getBankCard()));

        if (bankCard.getBalance().subtract(order.getShippingPriceInCents()).compareTo(BigDecimal.ZERO) < 0){
            throw  new CanNotPayException("no money to pay for order with id=" + order.getId());
        }

        User user = userService.findUserById(orderCheckDto.getUserId());

        OrderCheck orderCheck = OrderCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .order(order)
                .build();

        BankCard bankCardToSend = bankCardRepository
                .findBankCardByIdAndExpMonthAndExpYearAndCcv(ACCOUNT_TO_SEND_MONEY_ID, ACCOUNT_TO_SEND_MONEY_EXP_MONTH,
                        ACCOUNT_TO_SEND_MONEY_EXP_YEAR, ACCOUNT_TO_SEND_MONEY_CCV)
                .orElseThrow(() ->new BankException("no bankCard to send money"));
        processPaying(orderCheck, order, bankCardToSend);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processPaying(OrderCheck orderCheck, Order order, BankCard bankCard) throws BankException {
        BigDecimal moneyToPay = order.getShippingPriceInCents();

        sendMoney(orderCheck.getBankCard().getId(), bankCard.getId(), moneyToPay);
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
    public BankCardDto findBankCardDtoById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .map(BankCardMapper.INSTANCE::bankCardToDto)
                .orElseThrow(() -> new BankException("no bank card with id=" + id));
    }

    @Override
    public BankCard findBankCardById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("no bank card with id=" + id));
    }
}
