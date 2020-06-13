package ua.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.utility.GlobalExceptionHandler;
import ua.training.dto.DestinationDto;
import ua.training.dto.OrderDto;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.Order;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.DestinationService;
import ua.training.service.OrderService;
import ua.training.service.OrderTypeService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShipmentsControllerTest {

    @Mock
    OrderService orderService;

    @Mock
    OrderTypeService orderTypeService;

    @Mock
    DestinationService destinationService;

    @InjectMocks
    ShipmentsController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void shipmentsPageAll() throws Exception {

        List<OrderDto> orderDtoList = Arrays.asList(new OrderDto(), new OrderDto());

        when(orderService.findAllUserOrders(anyLong())).thenReturn(orderDtoList);

        User user = User.builder().id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/shipments/show/1/all")
                .flashAttr("user", user)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("orderDto"))
                .andExpect(model().attribute("orders", hasSize(orderDtoList.size())));

        verify(orderService).findAllUserOrders(anyLong());
    }

    @Test
    void shipmentsPageNotPaid() throws Exception {
        List<OrderDto> orderDtoList = Arrays.asList(new OrderDto(), new OrderDto());

        when(orderService.findAllNotPaidUserOrders(anyLong())).thenReturn(orderDtoList);

        User user = User.builder().id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/shipments/show/1/not_paid")
                .flashAttr("user", user)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("orderDto"))
                .andExpect(model().attribute("orders", hasSize(orderDtoList.size())));

        verify(orderService).findAllNotPaidUserOrders(anyLong());
    }

    @Test
    void shipmentsPageDelivered() throws Exception {
        List<OrderDto> orderDtoList = Arrays.asList(new OrderDto(), new OrderDto());

        when(orderService.findAllDeliveredUserOrders(anyLong())).thenReturn(orderDtoList);

        User user = User.builder().id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/shipments/show/1/delivered")
                .flashAttr("user", user)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("orderDto"))
                .andExpect(model().attribute("orders", hasSize(orderDtoList.size())));

        verify(orderService).findAllDeliveredUserOrders(anyLong());
    }

    @Test
    void shipmentsPageArchived() throws Exception {
        List<OrderDto> orderDtoList = Arrays.asList(new OrderDto(), new OrderDto());

        when(orderService.findAllArchivedUserOrders(anyLong())).thenReturn(orderDtoList);

        User user = User.builder().id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/shipments/show/1/archived")
                .flashAttr("user", user)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("orderDto"))
                .andExpect(model().attribute("orders", hasSize(orderDtoList.size())));

        verify(orderService).findAllArchivedUserOrders(anyLong());
    }

    @Test
    void findOrderById() throws Exception {

        when(orderService.getOrderDtoByIdAndUserId(anyLong(), anyLong())).thenReturn(new OrderDto());

        User user = User.builder()
                .id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/shipments/find_order")
                .flashAttr("user", user)
                .flashAttr("orderDto", OrderDto.builder().id(2L).build())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attribute("orders", hasSize(1)));

        verify(orderService).getOrderDtoByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void findOrder() throws Exception {
        when(orderService.getOrderDtoById(anyLong())).thenReturn(new OrderDto());

        mockMvc.perform(get("/shipments/find_order/1")
                .flashAttr("orderDto", OrderDto.builder().id(2L).build())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_shipments"))
                .andExpect(model().attribute("orders", hasSize(1)))
                .andExpect(model().attributeExists("orderDto"));

        verify(orderService).getOrderDtoById(anyLong());
    }

    @Test
    void createOrderView() throws Exception {

        List<DestinationDto> destinationDto = Arrays.asList(new DestinationDto(), new DestinationDto());

        List<OrderTypeDto> orderTypeDtoList = Arrays.asList(new OrderTypeDto(), new OrderTypeDto(), new OrderTypeDto());

        when(destinationService.getAllDestinationDto()).thenReturn(destinationDto);
        when(orderTypeService.getAllOrderTypeDto()).thenReturn(orderTypeDtoList);

        mockMvc.perform(get("/shipments/create_shipment"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/new_order"))
                .andExpect(model().attributeExists("newOrder"))
                .andExpect(model().attribute("types", hasSize(orderTypeDtoList.size())))
                .andExpect(model().attributeExists("destinationsFrom"))
                .andExpect(model().attributeExists("destinationsTo"));

        verify(destinationService).getAllDestinationDto();
        verify(orderTypeService).getAllOrderTypeDto();

    }

    @Test
    void createOrder() throws Exception {
        User user = User.builder()
                .id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .orders(Arrays.asList(new Order(), new Order()))
                .password("3848password")
                .email("email@g.dd").build();

        OrderDto orderDto = OrderDto.builder()
                .id(2L)
                .description("description")
                .destinationCityFrom("destinationCityFrom")
                .destinationCityTo("to")
                .type("2")
                .weight(BigDecimal.valueOf(77)).build();

        mockMvc.perform(post("/shipments/create_shipment")
                .flashAttr("newOrder", orderDto)
                .flashAttr("user", user)
        )
                .andExpect(view().name("redirect:/shipments/show/1/all"))
                .andExpect(status().is3xxRedirection());

        verify(orderService).createOrder(any(), any());
    }

    @Test
    void getMoveToArchive() throws Exception {
        mockMvc.perform(get("/shipments/archive_order/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/shipments/show/1/all"));

        verify(orderService).moveOrderToArchive(anyLong());
    }

    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(get("/shipments/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/shipments/show/1/all"));

        verify(orderService).deleteOrderById(anyLong());
    }

    @Test
    void handleOrderNotFoundException() throws Exception {
        when(orderService.getOrderDtoById(anyLong())).thenThrow(new OrderNotFoundException("can not find"));

        mockMvc.perform(get("/shipments/find_order/1")
                .flashAttr("orderDto", OrderDto.builder().id(2L).build())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", true))
                .andExpect(view().name("redirect:/shipments/show/1/all"));

        verify(orderService).getOrderDtoById(anyLong());
    }
}