package ua.training.service;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.controller.RegException;
import ua.training.dto.OrderDTO;
import ua.training.dto.OrdersDTO;
import ua.training.dto.UserDTO;
import ua.training.dto.UsersDTO;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderType;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrdersDTO getAllOrders() {
        return new OrdersDTO(orderRepository.findAll());
    }

    public void createOrder(OrderDTO orderDTO, User user) {
        Order order = Order.builder()
                .description(orderDTO.getDtoDescription())
                //   .destination(order.getDestination())
                .orderType(getOrderType(orderDTO))
                //  .shippingDate(order.getShippingDate())
                .weight(orderDTO.getDtoWeight())
                //  .status(order.getStatus())
                .ownerId(user)
                .announcedPrice(orderDTO.getDtoAnnouncedPrice())
                //  .isReturnShipping(order.isReturnShipping())
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

    public Optional<Order> getOrderById(@NotNull Long id) {
        return orderRepository.findById(id);
    }
}
