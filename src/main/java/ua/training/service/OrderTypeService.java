package ua.training.service;

import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

import java.util.List;

public interface OrderTypeService {

    List<OrderTypeDto> getAllOrderTypeDto();

    OrderType getOrderTypeById(Long id);

}
