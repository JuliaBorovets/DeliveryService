package ua.training.service;

import ua.training.dto.OrderCheckDto;

import java.util.List;

public interface OrderCheckService {


    List<OrderCheckDto> showAllChecks();

    List<OrderCheckDto> showChecksByUser(Long userId);
}
