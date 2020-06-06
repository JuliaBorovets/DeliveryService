package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.StatisticsDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.repository.OrderCheckRepository;
import ua.training.repository.OrderRepository;
import ua.training.service.AdminService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;
    private final OrderCheckRepository orderCheckRepository;

    public AdminServiceImpl(OrderRepository orderRepository, OrderCheckRepository orderCheckRepository) {
        this.orderRepository = orderRepository;
        this.orderCheckRepository = orderCheckRepository;
    }


    @Transactional
    @Override
    public void shipAllOrders(){

        List<Order> orders = orderRepository.findOrderByStatus(Status.PAID);

        orders.forEach(order -> {

            Long daysToDeliver = order.getDestination().getDaysToDeliver();

            order.setShippingDate(LocalDate.now().plusDays(1L));

            order.setDeliveryDate(LocalDate.now().plusDays(daysToDeliver));

            order.setStatus(Status.SHIPPED);

        });
    }

    @Transactional
    @Override
    public void shipOrder(Long orderId) throws OrderNotFoundException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("can not find destination"));

        Long daysToDeliver = order.getDestination().getDaysToDeliver();

        order.setShippingDate(LocalDate.now().plusDays(1L));

        order.setDeliveryDate(LocalDate.now().plusDays(daysToDeliver));

        order.setStatus(Status.SHIPPED);

    }

    @Override
    public StatisticsDto createStatisticsDto() {

        BigDecimal earningsLastMonth = orderCheckRepository
                .earningsByCreationMonthsAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());

        BigDecimal earningsYear = orderCheckRepository.earningsByCreationYear(LocalDate.now().getYear());

        Long deliversNumber = orderCheckRepository
                .ordersByCreationMonthsAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());

        Long deliversNumberYear = orderCheckRepository
                .ordersByCreationYear(LocalDate.now().getYear());

        return StatisticsDto.builder()
                .earningsLastMonth(earningsLastMonth)
                .earningsYear(earningsYear)
                .deliversNumber(deliversNumber)
                .deliversNumberYear(deliversNumberYear)
                .build();
    }


    @Override
    public Map<Integer, Long> statisticsNumberOfOrdersByForYear(Integer year) {
        Map<Integer, Long> statistic = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            statistic.put(i, orderCheckRepository.ordersByCreationMonthsAndYear(i, year));
        }
        return statistic;
    }

    @Override
    public Map<Integer, BigDecimal> statisticsEarningsOfOrdersByForYear(Integer year) {
        Map<Integer, BigDecimal> statistic = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            statistic.put(i, orderCheckRepository.earningsByCreationMonthsAndYear(i, year));
        }

        return statistic;
    }

}


