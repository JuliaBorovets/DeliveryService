package ua.training.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.controller.exception.BankTransactionException;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.*;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Getter
@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllOrders(long userId) {
        return orderRepository.findOrderByOwnerId(userId);
    }

    public void createOrder(OrderDTO orderDTO, User user) {
        Order order = Order.builder()
                .description(orderDTO.getDtoDescription())
                .destination(getDestination(orderDTO))
                .orderType(getOrderType(orderDTO))
                .shippingDate(LocalDate.now(ZoneId.of("Europe/Kiev")).plusDays(2))
                .weight(orderDTO.getDtoWeight())
                .owner(user)
                .announcedPrice(orderDTO.getDtoAnnouncedPrice())
                .orderStatus(OrderStatus.NOT_PAID)
                .shippingPrice(calculatePrice(orderDTO))
                .build();
        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException();
        }
    }

    private OrderType getOrderType(OrderDTO dto) {
        return OrderType.valueOf(dto.getDtoOrderType());
    }

    private Destination getDestination(OrderDTO dto) {
        return Destination.valueOf(dto.getDtoDestination());
    }

    private int getDestinationPrice(OrderDTO orderDTO) {
        return Destination.valueOf(orderDTO.getDtoDestination()).getPriceForDestination();
    }

    private int getTypePrice(OrderDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getDtoOrderType()).getPriceForType();
    }

    public BigDecimal calculatePrice(OrderDTO orderDTO) {
        return BigDecimal.valueOf(ShipmentsTariffs.BASE_PRICE + (getDestinationPrice(orderDTO) + getTypePrice(orderDTO))
                * ShipmentsTariffs.COEFFICIENT + orderDTO.getDtoAnnouncedPrice().doubleValue() *
                ShipmentsTariffs.COEFFICIENT_FOR_ANN_PRICE);
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

    public List<Order> findAllOrders() {

        return orderRepository.findAll();
    }

    public List<Order> findAllPaidOrders() {
        return orderRepository.findOrderByOrderStatus(OrderStatus.PAID);
    }


    public void orderSetShippedStatus(Order order) {
        if (order.getOrderStatus().equals(OrderStatus.PAID)) {
            order.setOrderStatus(OrderStatus.SHIPPED);
            order.setDeliveryDate(LocalDate.now(ZoneId.of("Europe/Kiev")).plusDays(findDeliveryDays(order).getDay()));
            orderRepository.save(order);
        }
    }

    private DeliveryDate findDeliveryDays(Order order) {
        switch (order.getDestination()) {
            case NONE:
                return DeliveryDate.IN_A_MOMENT;
            case INNER:
                return DeliveryDate.QUICKLY;
            case COUNTRY:
                return DeliveryDate.LONG;
        }
        return DeliveryDate.LONG;
    }
}

