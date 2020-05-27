package ua.training.service;

import ua.training.dto.OrderCheckDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderCheckService {


    List<OrderCheckDto> showAllChecks();

    List<OrderCheckDto> showChecksByUser(Long userId);

    List<OrderCheckDto> showChecksForMonthOfYear(LocalDate localDate);

    List<OrderCheckDto> showChecksForYear(LocalDate localDate);
}
