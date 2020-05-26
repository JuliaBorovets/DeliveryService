package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.converters.DtoToOrderConverter;
import ua.training.converters.DtoToUserConverter;
import ua.training.converters.OrderToDtoConverter;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;
import ua.training.repository.OrderRepository;
import ua.training.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderToDtoConverter orderToDtoConverter;
    private final DtoToOrderConverter dtoToOrderConverter;
    private final DtoToUserConverter dtoToUserConverter;

    public OrderServiceImpl(OrderRepository orderRepository,  OrderToDtoConverter orderToDtoConverter,
                            DtoToOrderConverter dtoToOrderConverter, DtoToUserConverter dtoToUserConverter) {
        this.orderRepository = orderRepository;
        this.orderToDtoConverter = orderToDtoConverter;
        this.dtoToOrderConverter = dtoToOrderConverter;
        this.dtoToUserConverter = dtoToUserConverter;
    }

    public List<OrderDto> findAllUserOrder(Long userId) {

        return orderRepository.findOrderByOwnerId(userId).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public List<OrderDto> findAllPaidOrdersDTO() {

        return orderRepository.findOrderByStatus(Status.PAID).stream()
                .map(orderToDtoConverter::convert)
                .collect(Collectors.toList());

    }


    private BigDecimal calculatePrice(OrderDto orderDTO) {
        return BigDecimal.valueOf(30);

    }

    @Override
    public OrderDto getOrderDtoById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("order " + id + " not found"));

        return orderToDtoConverter.convert(order);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = OrderCreateException.class)
    @Override
    public void createOrder(OrderDto orderDTO, UserDto userDto) throws OrderCreateException {
        Order orderToSave = dtoToOrderConverter.convert(orderDTO);

        Objects.requireNonNull(orderToSave).setOwner(dtoToUserConverter.convert(userDto));
        orderToSave.setShippingPriceInCents(calculatePrice(orderDTO));

        try {
            orderRepository.save(orderToSave);
        } catch (DataIntegrityViolationException e) {
            throw new OrderCreateException("Can not create order");
        }
    }

}

