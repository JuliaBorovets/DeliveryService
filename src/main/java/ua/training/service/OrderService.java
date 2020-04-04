package ua.training.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.*;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Page<OrderDTO> findAllUserOrder(Long userId, Pageable pageable) {

        return new PageImpl<>(orderRepository.findOrderByOwnerId(pageable, userId)
                .getContent()
                .stream()
                .map(order -> OrderDTO.builder()
                        .dtoId(order.getId())
                        .dtoOrderType(order.getOrderType())
                        .dtoDestination(order.getDestination())
                        .dtoOrderStatus(order.getOrderStatus())
                        .dtoOwner(order.getOwner())
                        .dtoWeight(order.getWeight())
                        .shippingDate(order.getShippingDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(LocaleContextHolder.getLocale())))
                        .deliveryDate(order.getOrderStatus().equals(OrderStatus.SHIPPED) ? order.getDeliveryDate().
                                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                        .withLocale(LocaleContextHolder.getLocale())) : " ")
                        .dtoShippingPrice(isLocaleUa() ? order.getShippingPriceUkr() : order.getShippingPriceEn())
                        .build())
                .collect(Collectors.toList()));

    }

    public Page<OrderDTO> findAllPaidOrdersDTO(Pageable pageable) {

        return new PageImpl<>(orderRepository
                .findOrderByOrderStatus(pageable, OrderStatus.PAID)
                .getContent()
                .stream()
                .map(order -> OrderDTO.builder()
                        .dtoId(order.getId())
                        .dtoOrderType(order.getOrderType())
                        .dtoDestination(order.getDestination())
                        .dtoOrderStatus(order.getOrderStatus())
                        .dtoOwner(order.getOwner())
                        .dtoWeight(order.getWeight())
                        .shippingDate(order.getShippingDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(LocaleContextHolder.getLocale())))
                        .dtoShippingPrice(isLocaleUa() ? order.getShippingPriceUkr() : order.getShippingPriceEn())
                        .build())
                .collect(Collectors.toList()));

    }


    private boolean isLocaleUa() {
        return LocaleContextHolder.getLocale().equals(new Locale("uk"));
    }


    public void createOrder(OrderDTO orderDTO, User user) throws OrderCreateException {

        Order order = Order.builder()
                .destination(orderDTO.getDtoDestination())
                .orderType(orderDTO.getDtoOrderType())
                .orderStatus(OrderStatus.NOT_PAID)
                .shippingDate(LocalDate.now())
                .weight(orderDTO.getDtoWeight())
                .owner(user)
                .shippingPriceUkr(calculatePrice(orderDTO))
                .shippingPriceEn(convertPriceToLocale(calculatePrice(orderDTO), new Locale("en")))
                .build();
        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new OrderCreateException("Can not create order");
        }
    }

    private BigDecimal convertPriceToLocale(BigDecimal price, Locale locale) {
        return isLocaleUa() ? price : price.divide(ShipmentsTariffs.DOLLAR, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal convertPriceFromLocale(BigDecimal price) {
        return isLocaleUa() ? price : price.multiply(ShipmentsTariffs.DOLLAR);
    }


    public BigDecimal calculatePrice(OrderDTO orderDTO) {
        return BigDecimal.valueOf(ShipmentsTariffs.BASE_PRICE + (orderDTO.getDtoDestination().getPriceForDestination()
                + orderDTO.getDtoOrderType().getPriceForType()) * ShipmentsTariffs.COEFFICIENT);
    }

    private Long getAdminAccount() {
        User admin = userRepository.findUserByRole(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new UsernameNotFoundException("no admin"));

        return admin.getId();
    }

    public void payForOrder(Order order) throws BankTransactionException {
        if (!isShipped(order) && !isPaid(order)) {
            BigDecimal amount = order.getShippingPriceUkr();
            sendMoney(order.getOwner().getId(), getAdminAccount(), amount);
            order.setOrderStatus(OrderStatus.PAID);
            orderRepository.save(order);
        } else throw new BankTransactionException("order is already paid");
    }


    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("order " + id + " not found"));
    }

    public boolean isPaid(Order order) {

        return order.getOrderStatus().equals(OrderStatus.PAID);
    }

    public boolean isShipped(Order order) {

        return order.getOrderStatus().equals(OrderStatus.SHIPPED);
    }



    public void orderSetShippedStatus(Long id) throws OrderNotFoundException {
        Order order = getOrderById(id);

        if (isPaid(order)) {
            order.setOrderStatus(OrderStatus.SHIPPED);
            order.setDeliveryDate(LocalDate.now().plusDays(order.getDestination().getDay()));
            orderRepository.save(order);
        }
    }


    public void addAmount(Long id, BigDecimal amount) throws BankTransactionException {
        User account = userService.findUserById(id);
        BigDecimal newBalance = account.getBalance().add(convertPriceFromLocale(amount));
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankTransactionException("no money");
        }
        account.setBalance(newBalance);
        userRepository.save(account);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankTransactionException.class)
    void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankTransactionException {
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, amount.negate());
    }


}

