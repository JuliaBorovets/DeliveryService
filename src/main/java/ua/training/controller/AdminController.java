package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.AdminService;
import ua.training.service.OrderCheckService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final OrderService orderService;
    private final AdminService adminService;
    private final UserService userService;
    private final OrderCheckService orderCheckService;

    public AdminController(OrderService orderService, AdminService adminService, UserService userService,
                           OrderCheckService orderCheckService) {
        this.orderService = orderService;
        this.adminService = adminService;
        this.userService = userService;
        this.orderCheckService = orderCheckService;
    }

    @ModelAttribute
    public void setModel(@AuthenticationPrincipal User user,  @RequestParam(required = false) String error, Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));
    }

    @GetMapping("/to_ship")
    public String shipPage(Model model) {

        List<OrderDto> orders = orderService.findAllPaidOrdersDTO();
        model.addAttribute("order", orders);

        return "admin/ship_page";
    }

    @GetMapping(value = "/to_ship/{id}")
    public String shipOneOrder(@PathVariable Long id, @PageableDefault Pageable pageable) throws OrderNotFoundException {

        adminService.shipOrder(id);

        return "redirect:/admin/to_ship";

    }

    @GetMapping("/to_deliver")
    public String deliverPage(Model model) {

        List<OrderDto> orders = orderService.findAllShippedOrdersDTO();
        model.addAttribute("order", orders);

        return "admin/deliver_page";
    }

    @GetMapping(value = "/to_deliver/{id}")
    public String deliverOneOrder(@PathVariable Long id, @PageableDefault Pageable pageable) throws OrderNotFoundException {

        adminService.deliverOrder(id);

        return "redirect:/admin/to_deliver";

    }

    @GetMapping("/to_receive")
    public String receivePage(Model model) {

        List<OrderDto> orders = orderService.findAllDeliveredOrdersDto();
        model.addAttribute("order", orders);

        return "admin/receive_page";
    }

    @GetMapping(value = "/to_receive/{id}")
    public String receiveOneOrder(@PathVariable Long id, @PageableDefault Pageable pageable) throws OrderNotFoundException {

        adminService.receiveOrder(id);

        return "redirect:/admin/to_receive";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model){

        model.addAttribute("statistics", adminService.createStatisticsDto());

        model.addAttribute("orders", adminService.statisticsNumberOfOrdersByForYear(LocalDate.now().getYear()));

        model.addAttribute("earnings", adminService.statisticsEarningsOfOrdersByForYear(LocalDate.now().getYear()));

        return "admin/statistics";
    }


    @GetMapping("/users_list")
    public String changeRoles(Model model){

        model.addAttribute("userDto", UserDto.builder().build());
        model.addAttribute("users", userService.findAllUserDto());

        return "admin/users_list";
    }


    @GetMapping("/find_user")
    public String findUserByLogin(@ModelAttribute UserDto userDto, Model model){

        if (userDto.getLogin()==null){
            userDto.setLogin("");
        }

        List<UserDto> users = userService.findAllByLoginLike(userDto.getLogin());

        model.addAttribute("users", users);

        return "admin/users_list";
    }

    @GetMapping("/change_roles/{userId}")
    public String getChangeUserRoles(@PathVariable Long userId,  Model model){

        UserDto userDto = userService.findUserDTOById(userId);
        model.addAttribute("userDto", userDto);
        model.addAttribute("isAdmin", userDto.getRole().equals(RoleType.ROLE_ADMIN));

        return "admin/change_roles";

    }

    @PostMapping("/change_roles/{userId}")
    public String changeUserRoles(@PathVariable Long userId) {

        userService.changeRole(userId);

        return "redirect:/admin/change_roles/" + userId;
    }

    @GetMapping("/show_checks")
    public String showAllChecks( @RequestParam(required = false) String error, Model model){

        model.addAttribute("error", error != null);
        model.addAttribute("checkDto", OrderCheckDto.builder().build());
        model.addAttribute("checks", orderCheckService.showAllChecks());

        return "admin/check_show";
    }

    @GetMapping("/find_check")
    public String findOrderCheckById(@ModelAttribute("checkDto") OrderCheckDto checkDto,
                                Model model) throws OrderCheckException {

        List<OrderCheckDto> checkDtoList = Collections.singletonList(orderCheckService.showCheckById(checkDto.getId()));
        model.addAttribute("checks", checkDtoList);

        return "admin/check_show";
    }

    @ExceptionHandler( OrderCheckException.class)
    public String handleOrderNotFoundException(Model model) {
        log.error("OrderNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/admin/show_checks";
    }
}
