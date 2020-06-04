package ua.training.service.serviceImpl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

//    @Mock
//    OrderRepository orderRepository;
//
//
//    @Mock
//    DtoToUserConverter dtoToUserConverter;
//
//    @InjectMocks
//    OrderServiceImpl service;
//
//    @Test
//    void findAllUserOrder() {
//
//        List<Order> orders = Arrays.asList(new Order(), new Order());
//        OrderDto orderDto = new OrderDto();
//
//        when(orderRepository.findOrderByOwnerId(anyLong())).thenReturn(orders);
//
//        when(OrderMapper.INSTANCE.orderToOrderDto(any())).thenReturn(orderDto);
//
//        List<OrderDto> foundOrders = service.findAllUserOrder(1L);
//
//        assertEquals(foundOrders.size(), orders.size());
//
//        verify(orderRepository).findOrderByOwnerId(anyLong());
//        verify(OrderMapper.INSTANCE, times(orders.size())).orderToOrderDto(any(Order.class));
//
//    }
//
//    @Test
//    void findAllPaidOrdersDTO() {
//
////        List<Order> orders = Arrays.asList(new Order(), new Order());
////        OrderDto orderDto = new OrderDto();
////
////        when(orderRepository.findOrderByStatus(any())).thenReturn(orders);
////
////        when(orderToDtoConverter.convert(any())).thenReturn(orderDto);
////
////
////        List<OrderDto> foundOrders = service.findAllPaidOrdersDTO();
////
////        assertEquals(foundOrders.size(), orders.size());
////
////        verify(orderRepository).findOrderByStatus(any());
////        verify(orderToDtoConverter, times(orders.size())).convert(any(Order.class));
//
//    }
//
//    @Test
//    void getOrderDtoById() throws OrderNotFoundException {
//
////        Order order = new Order();
////        order.setId(1L);
////
////        OrderDto orderDto = new OrderDto();
////        orderDto.setId(1L);
////
////        Optional<Order> optionalOrder = Optional.of(order);
////
////        when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
////        when(orderToDtoConverter.convert(any())).thenReturn(orderDto);
////
////        OrderDto foundOrder = service.getOrderDtoById(1L);
////
////        assertEquals(foundOrder.getId(), 1L);
////        verify(orderRepository).findById(anyLong());
////        verify(orderToDtoConverter).convert(any());
//
//    }
//
//    @Transactional
//    @Test
//    void createOrder() throws OrderCreateException {
//        final Long ID = 1L;
//        final Long USER_ID = 2L;
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setId(ID);
//
//        Order orderToSave = new Order();
//        orderToSave.setId(ID);
//
//        User owner = new User();
//        owner.setId(USER_ID);
//
//        UserDto ownerDto = new UserDto();
//        ownerDto.setId(USER_ID);
//
////        when(dtoToOrderConverter.convert(any(OrderDto.class))).thenReturn(orderToSave);
////        when(dtoToUserConverter.convert(any(UserDto.class))).thenReturn(owner);
////
////        service.createOrder(orderDto, ownerDto);
////
////        verify(orderRepository).save(any(Order.class));
////        verify(dtoToOrderConverter).convert(any());
////        verify(dtoToUserConverter).convert(any());
//
//    }

}