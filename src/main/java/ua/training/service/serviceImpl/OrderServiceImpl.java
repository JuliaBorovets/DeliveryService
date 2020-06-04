package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.entity.user.User;
import ua.training.mappers.OrderDtoToOrderConverter;
import ua.training.mappers.OrderMapper;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;
import ua.training.service.DestinationService;
import ua.training.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DestinationService destinationService;
    private final OrderDtoToOrderConverter orderConverter;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, DestinationService destinationService, OrderDtoToOrderConverter orderConverter, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.destinationService = destinationService;
        this.orderConverter = orderConverter;
        this.userRepository = userRepository;
    }

    public List<OrderDto> findAllUserOrder(Long userId) {

        return orderRepository.findOrderByOwnerId(userId).stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> findAllPaidOrdersDTO() {

        return orderRepository.findOrderByStatus(Status.PAID).stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());

    }


    private BigDecimal calculatePrice(Order order) {

        BigDecimal priceForDestination = order.getDestination().getPriceInCents();

        BigDecimal priceForWeight = order.getWeight().multiply(BigDecimal.valueOf(0.5));

        BigDecimal priceForType = order.getOrderType().getPriceInCents();

        return priceForDestination.add(priceForType).add(priceForWeight);

    }

    @Override
    public OrderDto getOrderDtoById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("order " + id + " not found"));

        return orderMapper.orderToOrderDto(order);
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order " + orderId + " not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = OrderCreateException.class)
    @Override
    public void createOrder(OrderDto orderDTO, User user) throws OrderCreateException {
        Order orderToSave = orderConverter.convert(orderDTO);

        User userToSave = userRepository.findUserById(user.getId()).orElseThrow(()->new
                RuntimeException("no user"));

        try {
            orderToSave.setStatus(Status.NOT_PAID);
            orderToSave.saveOrder(userToSave);
            orderToSave.setShippingDate(LocalDate.now());
            orderToSave.setShippingPriceInCents(calculatePrice(orderToSave));
            orderRepository.save(orderToSave);
        } catch (DataIntegrityViolationException e) {
            throw new OrderCreateException("Can not create order");
        }
    }

}

