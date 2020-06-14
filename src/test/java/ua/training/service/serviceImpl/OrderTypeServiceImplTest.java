package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.repository.OrderTypeRepository;

@ExtendWith(MockitoExtension.class)
class OrderTypeServiceImplTest {

    @Mock
    OrderTypeRepository orderTypeRepository;

    @InjectMocks
    OrderTypeServiceImpl service;

    @Test
    void getAllOrderTypeDto() {
        //todo
    }

    @Test
    void getOrderTypeById() {
        //todo
    }
}