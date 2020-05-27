package ua.training.service;

import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAllUserOrder(Long userId);

    void createOrder(OrderDto orderDTO, UserDto userDto) throws OrderCreateException;

    OrderDto getOrderDtoById(Long id) throws OrderNotFoundException;

    List<OrderDto> findAllPaidOrdersDTO();


}
