package ua.training.service;

import ua.training.dto.OrderDto;

import java.math.BigDecimal;

public interface CalculatorService {

    BigDecimal calculatePrice(OrderDto orderDto);
}
