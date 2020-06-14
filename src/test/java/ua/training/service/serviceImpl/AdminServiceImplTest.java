package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.repository.OrderCheckRepository;
import ua.training.repository.OrderRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderCheckRepository orderCheckRepository;

    @InjectMocks
    AdminServiceImpl service;

    @Test
    void shipOrder() throws OrderNotFoundException {

        Optional<Order> order = Optional.ofNullable(Order.builder()
                .id(2L)
                .destination(Destination.builder().daysToDeliver(3L).build()).build());

        when(orderRepository.findById(anyLong())).thenReturn(order);

        service.shipOrder(2L);

        assertNotNull(order.get().getShippingDate());
        assertNotNull(order.get().getDeliveryDate());
        assertEquals(order.get().getStatus(), Status.SHIPPED);
    }

    @Test
    void shipOrderException() {
        //todo checking status
    }

    @Test
    void deliverOrder() throws OrderNotFoundException {

        Optional<Order> order = Optional.ofNullable(Order.builder()
                .id(2L)
                .destination(Destination.builder().daysToDeliver(3L).build()).build());

        when(orderRepository.findById(anyLong())).thenReturn(order);

        service.deliverOrder(2L);

        assertEquals(order.get().getStatus(), Status.DELIVERED);
    }

    @Test
    void deliverOrderException() {
        //todo checking status
    }

    @Test
    void receiveOrder() throws OrderNotFoundException {

        Optional<Order> order = Optional.ofNullable(Order.builder()
                .id(2L)
                .destination(Destination.builder().daysToDeliver(3L).build()).build());

        when(orderRepository.findById(anyLong())).thenReturn(order);

        service.receiveOrder(2L);

        assertEquals(order.get().getStatus(), Status.RECEIVED);

    }

    @Test
    void receiveOrderException() {
        //todo checking status

    }

    @Test
    void createStatisticsDto() {
        //todo
    }

    @Test
    void statisticsNumberOfOrdersByForYear() {
        //todo
    }

    @Test
    void statisticsEarningsOfOrdersByForYear() {
        //todo
    }
}