package ua.training.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.repository.OrderRepository;
import ua.training.service.AdminService;

import java.time.LocalDate;
import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;

    public AdminServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

}
