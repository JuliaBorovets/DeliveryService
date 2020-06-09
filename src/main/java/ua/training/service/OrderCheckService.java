package ua.training.service;

import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;

import java.util.List;

public interface OrderCheckService {


    List<OrderCheckDto> showAllChecks();

    OrderCheckDto showCheckById(Long checkId) throws OrderCheckException;

    List<OrderCheckDto> showChecksByUser(Long userId);

    OrderCheckDto createCheckDto(Long orderDtoId, BankCardDto bankCardDtoId, Long userId) throws OrderNotFoundException;
}
