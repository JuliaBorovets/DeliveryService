package ua.training.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderType;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import javax.validation.constraints.NotNull;
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

    public Optional<Order> getOrderById(@NotNull Long id) {
        return orderRepository.findById(id);
    }
}
