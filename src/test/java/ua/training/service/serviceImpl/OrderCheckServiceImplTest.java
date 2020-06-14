package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.entity.user.User;
import ua.training.mappers.OrderCheckMapper;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCheckServiceImplTest {

    @Mock
    OrderCheckRepository orderCheckRepository;

    @Mock
    OrderService orderService;

    @Mock
    UserService userService;

    @InjectMocks
    OrderCheckServiceImpl service;

    @Test
    void showAllChecks() {
        Iterable<OrderCheck> orderCheckIterable = Arrays.asList(OrderCheck.builder().id(1L).build(), OrderCheck.builder().id(2L).build());
        when(orderCheckRepository.findAll()).thenReturn(orderCheckIterable);

        List<OrderCheck> orderCheckList = new ArrayList<>();
        orderCheckIterable.forEach(orderCheckList::add);

        List<OrderCheckDto> orderCheckDtoList = orderCheckList.stream()
                .map(OrderCheckMapper.INSTANCE::orderCheckToOrderCheckDto)
                .collect(Collectors.toList());

        List<OrderCheckDto> result = service.showAllChecks();

        assertEquals(result.size(), orderCheckDtoList.size());
        assertEquals(result.get(0).getId(), orderCheckDtoList.get(0).getId());
        assertEquals(result.get(1).getId(), orderCheckDtoList.get(1).getId());

        verify(orderCheckRepository).findAll();
    }

    @Test
    void showCheckById() throws OrderCheckException {
        Optional<OrderCheck> optionalOrderCheck = Optional.of(OrderCheck.builder().id(1L).build());

        when(orderCheckRepository.findById(anyLong())).thenReturn(optionalOrderCheck);

        OrderCheckDto result = service.showCheckById(1L);

        assertEquals(result.getId(), optionalOrderCheck.get().getId());

        verify(orderCheckRepository).findById(anyLong());
    }

    @Test
    void showChecksByUser() {
        OrderCheck orderCheck1 = OrderCheck.builder().id(1L).user(User.builder().id(3L).build()).build();
        OrderCheck orderCheck2 = OrderCheck.builder().id(2L).user(User.builder().id(4L).build()).build();


        List<OrderCheck> orderCheckList = Arrays.asList(orderCheck1, orderCheck2);
        when(orderCheckRepository.findAllByUser_Id(anyLong())).thenReturn(orderCheckList);

        List<OrderCheckDto> orderCheckDtoList = orderCheckList.stream()
                .map(OrderCheckMapper.INSTANCE::orderCheckToOrderCheckDto)
                .collect(Collectors.toList());

        List<OrderCheckDto> result = service.showChecksByUser(3L);

        assertEquals(result.size(), orderCheckDtoList.size());
        assertEquals(result.get(0).getUserId(), orderCheckDtoList.get(0).getUserId());
        assertEquals(result.get(1).getUserId(), orderCheckDtoList.get(1).getUserId());

        verify(orderCheckRepository).findAllByUser_Id(anyLong());

    }

    @Test
    void createCheckDto() throws OrderNotFoundException {
        Long orderDtoId = 1L;
        BankCardDto bankCardDto = BankCardDto.builder().id(2L).build();
        Long userId = 3L;

        when(orderService.getOrderDtoById(anyLong())).thenReturn(OrderDto.builder()
                .id(orderDtoId)
                .shippingPriceInCents(BigDecimal.ONE).build());

        when(userService.findUserDTOById(anyLong())).thenReturn(UserDto.builder().id(userId).build());

        OrderCheckDto result = service.createCheckDto(orderDtoId, bankCardDto, userId);

        assertEquals(result.getOrderId(), orderDtoId);
        assertEquals(result.getBankCard(), bankCardDto.getId());
        assertEquals(result.getPriceInCents(), BigDecimal.ONE);
        assertEquals(result.getUserId(), userId);
        assertEquals(result.getStatus(), Status.NOT_PAID);

        verify(orderService).getOrderDtoById(anyLong());
        verify(userService).findUserDTOById(anyLong());
    }
}