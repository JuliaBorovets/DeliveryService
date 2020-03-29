package ua.training.service;

import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public List<OrderDTO> orderDTOList(Long userId, boolean isLocaleEN) {
        return orderRepository
                .findOrderByOwnerId(userId)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }


    public List<OrderDTO> findAllPaidOrdersDTO() {
        return orderRepository
                .findOrderByOrderStatus(OrderStatus.PAID)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }


    public void createOrder(OrderDTO orderDTO, User user) throws OrderCreateException {

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
            throw new OrderCreateException("Can not create order");
        }
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
            order.setOrderStatus(OrderStatus.PAID);
            BigDecimal amount = order.getShippingPrice();
            sendMoney(order.getOwner().getId(), getAdminAccount(), amount);
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
            order.setDeliveryDate(LocalDate.now(ZoneId.of("Europe/Kiev")).plusDays(order.getDestination().getDay()));
            orderRepository.save(order);
        }
    }


    public void addAmount(Long id, BigDecimal amount) throws BankTransactionException {
        User account = userService.findUserById(id);
        BigDecimal newBalance = account.getBalance().add(amount);
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


    public Page<OrderDTO> findPaginated(User user, Pageable pageable, boolean isLocaleEN) {

        List<OrderDTO> orders = orderDTOList(user.getId(), isLocaleEN)
                .stream()
                .sorted(Comparator.comparing(OrderDTO::getDtoId)
                        .reversed())
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<OrderDTO> list;

        if (orders.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, orders.size());
            list = orders.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), orders.size());
    }

}

