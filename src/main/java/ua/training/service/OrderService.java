package ua.training.service;

import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.exception.UserNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;
import ua.training.entity.user.User;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAllUserOrders(Long userId);

    List<OrderDto> findAllNotPaidUserOrders(Long userId);

    List<OrderDto> findAllArchivedUserOrders(Long userId);

    List<OrderDto> findAllDeliveredUserOrders(Long userId);

    void createOrder(OrderDto orderDTO, User user) throws OrderCreateException, UserNotFoundException;

    OrderDto getOrderDtoById(Long id) throws OrderNotFoundException;

    OrderDto getOrderDtoByIdAndUserId(Long id, Long userId) throws OrderNotFoundException;

    List<OrderDto> findAllPaidOrdersDTO();

    List<OrderDto> findAllShippedOrdersDTO();

    List<OrderDto> findAllDeliveredOrdersDto();

    Order findOrderById(Long orderId) throws OrderNotFoundException;

    void moveOrderToArchive(Long orderId) throws OrderNotFoundException;

    void deleteOrderById(Long orderId) throws OrderNotFoundException;
}
