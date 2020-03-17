package ua.training.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderType;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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
                .shippingDate(LocalDate.now(ZoneId.of("Europe/Kiev")).plusDays(1))
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

    public Optional<Order> getOrderById(@NotNull Long id) {
        return orderRepository.findById(id);
    }

}
