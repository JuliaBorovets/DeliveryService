package ua.training.service;

import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.StatisticsDto;

import java.math.BigDecimal;
import java.util.Map;

public interface AdminService {

    void shipOrder(Long orderId) throws OrderNotFoundException;

    void deliverOrder(Long orderID) throws OrderNotFoundException;

    void receiveOrder(Long orderId) throws OrderNotFoundException;

    StatisticsDto createStatisticsDto();

    Map<Integer, Long> statisticsNumberOfOrdersByForYear(Integer year);

    Map<Integer, BigDecimal> statisticsEarningsOfOrdersByForYear(Integer year);

}
