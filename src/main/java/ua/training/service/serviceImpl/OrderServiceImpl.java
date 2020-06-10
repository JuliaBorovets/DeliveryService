package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.exception.UserNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.entity.user.User;
import ua.training.mappers.DtoToOrderConverter;
import ua.training.mappers.OrderToDtoConverter;
import ua.training.repository.OrderRepository;
import ua.training.repository.UserRepository;
import ua.training.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DtoToOrderConverter orderConverter;
    private final UserRepository userRepository;
    private final OrderToDtoConverter orderToDtoConverter;

    @Value("${constants.BASE.PRICE}")
    private BigDecimal BASE_PRICE;

    @Value("${constants.WEIGHT.COEFFICIENT}")
    private BigDecimal WEIGHT_COEFFICIENT;

    public OrderServiceImpl(OrderRepository orderRepository, DtoToOrderConverter orderConverter,
                            UserRepository userRepository, OrderToDtoConverter orderToDtoConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.userRepository = userRepository;
        this.orderToDtoConverter = orderToDtoConverter;
    }

    public List<OrderDto> findAllUserOrders(Long userId) {
        return orderRepository.findOrderByOwnerId(userId).stream()
                .map(orderToDtoConverter::convert)
                .filter(o -> !o.getStatus().equals(Status.ARCHIVED))
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> findAllNotPaidUserOrders(Long userId) {
        return orderRepository.findByStatusAndOwner_Id(Status.NOT_PAID, userId).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> findAllArchivedUserOrders(Long userId) {
        return orderRepository.findByStatusAndOwner_Id(Status.ARCHIVED, userId).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAllDeliveredUserOrders(Long userId) {
        return orderRepository.findByStatusAndOwner_Id(Status.DELIVERED, userId).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public List<OrderDto> findAllPaidOrdersDTO() {
        return orderRepository.findOrderByStatus(Status.PAID).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAllShippedOrdersDTO() {
        return orderRepository.findOrderByStatus(Status.SHIPPED).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAllDeliveredOrdersDto() {
        return orderRepository.findOrderByStatus(Status.DELIVERED).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    private BigDecimal calculatePrice(Order order) {

        BigDecimal priceForDestination = order.getDestination().getPriceInCents();

        BigDecimal priceForWeight = order.getWeight().multiply(WEIGHT_COEFFICIENT);

        BigDecimal priceForType = order.getOrderType().getPriceInCents();

        return priceForDestination.add(priceForType).add(priceForWeight).add(BASE_PRICE);

    }

    @Override
    public OrderDto getOrderDtoById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("order id=" + id + " not found"));

        return orderToDtoConverter.convert(order);
    }

    @Override
    public OrderDto getOrderDtoByIdAndUserId(Long id, Long userId) throws OrderNotFoundException {

        Order order = orderRepository.findByIdAndOwner_id(id, userId)
                .orElseThrow(() -> new OrderNotFoundException("order id=" + id + " , userId=" + userId + "  not found"));

        return orderToDtoConverter.convert(order);
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order " + orderId + " not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = OrderCreateException.class)
    @Override
    public void createOrder(OrderDto orderDTO, User user) throws OrderCreateException, UserNotFoundException {
        Order orderToSave = orderConverter.convert(orderDTO);

        User userToSave = userRepository.findUserById(user.getId())
                .orElseThrow(()->new UserNotFoundException("no user with id=" + user.getId()));

        try {
            orderToSave.setStatus(Status.NOT_PAID);
            orderToSave.saveOrder(userToSave);
            orderToSave.setShippingDate(LocalDate.now());
            orderToSave.setShippingPriceInCents(calculatePrice(orderToSave));
            orderRepository.save(orderToSave);
        } catch (DataIntegrityViolationException e) {
            throw new OrderCreateException("Can not create order with id = " + orderDTO.getId());
        }
    }


    @Override
    public void moveOrderToArchive(Long orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId);

        if (order.getStatus().equals(Status.RECEIVED)) {
            order.setStatus(Status.ARCHIVED);
            orderRepository.save(order);
        }
    }

    @Override
    public void deleteOrderById(Long orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId);

        if (order.getStatus().equals(Status.NOT_PAID)){
            orderRepository.delete(order);
        }

        log.info("deleting order");
    }
}

