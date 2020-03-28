package ua.training.service;

import lombok.Getter;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankTransactionException;
import ua.training.dto.AddMoneyDTO;
import ua.training.dto.OrderDTO;
import ua.training.dto.UserDTO;
import ua.training.entity.order.*;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Getter
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;


    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<OrderDTO> orderDTOList(Long userId) {
        return orderRepository
                .findOrderByOwnerId(userId)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }


    public void createOrder(OrderDTO orderDTO, User user) {

        Order order = Order.builder()
                .destination(orderDTO.getDtoDestination())
                .orderType(orderDTO.getDtoOrderType())
                .shippingDate(LocalDate.now())
                .weight(orderDTO.getDtoWeight())
                .owner(user)
                .orderStatus(OrderStatus.NOT_PAID)
                .shippingPrice(calculatePrice(orderDTO))
                .build();
        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException();
        }
    }


    public BigDecimal calculatePrice(OrderDTO orderDTO) {
        return BigDecimal.valueOf(ShipmentsTariffs.BASE_PRICE + (orderDTO.getDtoDestination().getPriceForDestination()
                + orderDTO.getDtoOrderType().getPriceForType()) * ShipmentsTariffs.COEFFICIENT);
    }


    public void payForOrder(Order order) throws BankTransactionException {
        if (!order.getOrderStatus().equals(OrderStatus.PAID)) {
            order.setOrderStatus(OrderStatus.PAID);
            orderRepository.save(order);
        } else throw new BankTransactionException("order is already paid");
    }


    public Order getOrderById(@NotNull Long id) {

        return orderRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id.toString()));
    }

    public boolean isPaid(OrderDTO order) {
        return order.getDtoOrderStatus().equals(OrderStatus.PAID);
    }

    public boolean isShipped(Order order) {
        return order.getOrderStatus().equals(OrderStatus.SHIPPED);

    }

    public List<Order> findAllPaidOrders() {

        return orderRepository.findOrderByOrderStatus(OrderStatus.PAID);
    }

    public List<OrderDTO> findAllPaidOrdersDTO() {

        return orderRepository
                .findOrderByOrderStatus(OrderStatus.PAID)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void orderSetShippedStatus(OrderDTO orderDTO) {
        Order order = orderRepository.findOrderById(orderDTO.getDtoId()).orElseThrow(() -> new UsernameNotFoundException(orderDTO.getDtoId().toString()));
        ;
        if (isPaid(orderDTO)) {
            order.setOrderStatus(OrderStatus.SHIPPED);
            order.setDeliveryDate(LocalDate.now(ZoneId.of("Europe/Kiev")).plusDays(orderDTO.getDtoDestination().getDay()));
            orderRepository.save(order);
        }
    }


    @Transactional
    public void addAmount(Long id, BigDecimal amount) throws BankTransactionException {
        User account = userRepository.findUserById(id).orElseThrow(() -> new UsernameNotFoundException(id.toString()));
        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankTransactionException("no money");
        }
        account.setBalance(newBalance);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankTransactionException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankTransactionException {
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, amount.negate());
    }


//    public Object listBankAccountInfo(User user) {
//        String sql = "Select new " + AddMoneyDTO.class.getName() + "(e.balance) " + " from " +
//                User.class.getName() + " e ";
//
//        Query query = (Query) entityManager.createQuery(sql, AddMoneyDTO.class);
//        return query.getResultList().get(user.getId().intValue() - 1);
//    }


}

