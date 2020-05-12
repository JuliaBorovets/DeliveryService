package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.OrderCheckService;

@Slf4j
@Service
public class OrderCheckServiceImpl implements OrderCheckService {

    private final OrderCheckRepository orderCheckRepository;

    public OrderCheckServiceImpl(OrderCheckRepository orderCheckRepository) {
        this.orderCheckRepository = orderCheckRepository;
    }
}
