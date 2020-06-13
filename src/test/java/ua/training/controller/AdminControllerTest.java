package ua.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.utility.GlobalExceptionHandler;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.dto.StatisticsDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.service.AdminService;
import ua.training.service.OrderCheckService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    OrderService orderService;

    @Mock
    AdminService adminService;

    @Mock
    UserService userService;

    @Mock
    OrderCheckService orderCheckService;

    @InjectMocks
    AdminController controller;

    List<OrderDto> orders;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        orders = Arrays.asList(new OrderDto(), new OrderDto(), new OrderDto());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shipPage() throws Exception {

        when(orderService.findAllPaidOrdersDTO()).thenReturn(orders);

        mockMvc.perform(get("/admin/to_ship"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/ship_page"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("order", hasSize(orders.size())));

        verify(orderService).findAllPaidOrdersDTO();
    }

    @Test
    void shipOneOrder() throws Exception {
        mockMvc.perform(get("/admin/to_ship/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/to_ship"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"));

        verify(adminService).shipOrder(1L);
    }

    @Test
    void deliverPage() throws Exception {

        when(orderService.findAllShippedOrdersDTO()).thenReturn(orders);

        mockMvc.perform(get("/admin/to_deliver"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/deliver_page"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("order", hasSize(orders.size())));

        verify(orderService).findAllShippedOrdersDTO();

    }

    @Test
    void deliverOneOrder() throws Exception {
        mockMvc.perform(get("/admin/to_deliver/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/to_deliver"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"));

        verify(adminService).deliverOrder(1L);

    }

    @Test
    void receivePage() throws Exception {
        when(orderService.findAllDeliveredOrdersDto()).thenReturn(orders);

        mockMvc.perform(get("/admin/to_receive"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/receive_page"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("order", hasSize(orders.size())));

        verify(orderService).findAllDeliveredOrdersDto();
    }

    @Test
    void receiveOneOrder() throws Exception {
        mockMvc.perform(get("/admin/to_receive/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/to_receive"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"));

        verify(adminService).receiveOrder(1L);
    }

    @Test
    void showStatistics() throws Exception {

        when(adminService.createStatisticsDto()).thenReturn(new StatisticsDto());
        when(adminService.statisticsNumberOfOrdersByForYear(anyInt())).thenReturn(new HashMap<>());
        when(adminService.statisticsEarningsOfOrdersByForYear(anyInt())).thenReturn(new HashMap<>());

        mockMvc.perform(get("/admin/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/statistics"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("statistics"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("earnings"));

        verify(adminService).createStatisticsDto();
        verify(adminService).statisticsNumberOfOrdersByForYear(anyInt());
        verify(adminService).statisticsEarningsOfOrdersByForYear(anyInt());
    }

    @Test
    void changeRoles() throws Exception {
        List<UserDto> userDto = Arrays.asList(new UserDto(), new UserDto());

        when(userService.findAllUserDto()).thenReturn(userDto);

        mockMvc.perform(get("/admin/users_list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users_list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("userDto"))
                .andExpect(model().attribute("users", hasSize(userDto.size())));

        verify(userService).findAllUserDto();
    }

    @Test
    void findUserByLogin() throws Exception {
        List<UserDto> userDto = Arrays.asList(new UserDto(), new UserDto());

        when(userService.findAllByLoginLike(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/admin/find_user"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users_list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("userDto"))
                .andExpect(model().attribute("users", hasSize(userDto.size())));

        verify(userService).findAllByLoginLike(anyString());
    }

    @Test
    void getChangeUserRoles() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).role(RoleType.ROLE_ADMIN).build();

        when(userService.findUserDTOById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/admin/change_roles/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/change_roles"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("isAdmin", true))
                .andExpect(model().attribute("userDto", userDto));

        verify(userService).findUserDTOById(anyLong());
    }

    @Test
    void changeUserRolesWithBindingErrors() throws Exception {

        mockMvc.perform(post("/admin/change_roles/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/change_roles/1"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"));

        verify(userService).changeRole(anyLong());

    }

    @Test
    void showAllChecks() throws Exception {
        List<OrderCheckDto> orderCheckDtoList = Arrays.asList(new OrderCheckDto(), new OrderCheckDto());
        when(orderCheckService.showAllChecks()).thenReturn(orderCheckDtoList);

        mockMvc.perform(get("/admin/show_checks"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/check_show"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("checkDto"))
                .andExpect(model().attribute("checks", hasSize(orderCheckDtoList.size())));

        verify(orderCheckService).showAllChecks();
    }

    @Test
    void findOrderCheckById() throws Exception {

        OrderCheckDto orderCheckDto = OrderCheckDto.builder().id(1L).build();
        when(orderCheckService.showCheckById(1L)).thenReturn(new OrderCheckDto());

        mockMvc.perform(get("/admin/find_check")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("checkDto", orderCheckDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/check_show"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("checks", hasSize(1)));

        verify(orderCheckService).showCheckById(anyLong());
    }


    @Test
    public void handleOrderNotFoundException() throws Exception {
        when(orderCheckService.showCheckById(anyLong())).thenThrow(OrderCheckException.class);

        OrderCheckDto orderCheckDto = OrderCheckDto.builder().id(1L).build();

        mockMvc.perform(get("/admin/find_check")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("checkDto", orderCheckDto)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/show_checks"))
                .andExpect(model().attribute("error", true));
    }

}