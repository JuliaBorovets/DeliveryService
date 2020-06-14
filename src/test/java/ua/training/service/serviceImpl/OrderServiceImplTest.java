package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.mappers.OrderMapper;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;
import ua.training.service.DestinationService;
import ua.training.service.OrderTypeService;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock
    OrderTypeService orderTypeService;

    @Mock
    DestinationService destinationService;

    @InjectMocks
    OrderServiceImpl service;

    @Test
    void findAllUserOrders() {
        //todo
    }

    @Test
    void findAllNotPaidUserOrders() {
        //todo
    }

    @Test
    void findAllArchivedUserOrders() {
        //todo
    }

    @Test
    void findAllDeliveredUserOrders() {
        //todo
    }

    @Test
    void findAllPaidOrdersDTO() {
        //todo
    }

    @Test
    void findAllShippedOrdersDTO() {
        //todo
    }

    @Test
    void findAllDeliveredOrdersDto() {
        //todo
    }

    @Test
    void getOrderDtoById() {
        //todo
    }

    @Test
    void getOrderDtoByIdAndUserId() {
        //todo
    }

    @Test
    void findOrderById() {
        //todo
    }

    @Test
    void createOrder() {
        //todo
    }

    @Test
    void moveOrderToArchive() {
        //todo
    }

    @Test
    void deleteOrderById() {
        //todo
    }
}