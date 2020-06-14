package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.mappers.OrderCheckMapper;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.OrderCheckService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderCheckServiceImpl implements OrderCheckService {

    private final OrderCheckRepository orderCheckRepository;
    private final OrderService orderService;
    private final UserService userService;

    public OrderCheckServiceImpl(OrderCheckRepository orderCheckRepository, OrderService orderService,
                                 UserService userService) {
        this.orderCheckRepository = orderCheckRepository;
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public List<OrderCheckDto> showAllChecks() {

        List<OrderCheck> orderChecks = new ArrayList<>();

        orderCheckRepository.findAll()
                .forEach(orderChecks::add);

        return orderChecks.stream()
                .map(OrderCheckMapper.INSTANCE::orderCheckToOrderCheckDto)
                .collect(Collectors.toList());
    }


    @Override
    public OrderCheckDto showCheckById(Long checkId) throws OrderCheckException {
        return OrderCheckMapper.INSTANCE.orderCheckToOrderCheckDto(orderCheckRepository
                .findById(checkId)
                .orElseThrow(()->new OrderCheckException("no check with id=" + checkId)));
    }

    @Override
    public List<OrderCheckDto> showChecksByUser(Long userId) {

        return orderCheckRepository
                .findAllByUser_Id(userId).stream()
                .map(OrderCheckMapper.INSTANCE::orderCheckToOrderCheckDto)
                .collect(Collectors.toList());
    }


    @Override
    public OrderCheckDto createCheckDto(Long orderDtoId, BankCardDto bankCardDto, Long userId)
            throws OrderNotFoundException {

        OrderDto orderDto = orderService.getOrderDtoById(orderDtoId);
        UserDto userDto = userService.findUserDTOById(userId);

        return OrderCheckDto.builder()
                .orderId(orderDtoId)
                .bankCard(bankCardDto.getId())
                .priceInCents(orderDto.getShippingPriceInCents())
                .userId(userDto.getId())
                .status(Status.NOT_PAID).build();
    }
}
