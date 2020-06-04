package ua.training.service;

import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;
import ua.training.entity.user.User;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAllUserOrder(Long userId);

    void createOrder(OrderDto orderDTO, User user) throws OrderCreateException;

    OrderDto getOrderDtoById(Long id) throws OrderNotFoundException;

    List<OrderDto> findAllPaidOrdersDTO();

    Order findOrderById(Long orderId) throws OrderNotFoundException;


}
