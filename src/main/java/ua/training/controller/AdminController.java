package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.AdminService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final OrderService orderService;
    private final AdminService adminService;
    private final UserService userService;

    public AdminController(OrderService orderService, AdminService adminService, UserService userService) {
        this.orderService = orderService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @ModelAttribute
    public void setModel(@AuthenticationPrincipal User user,  Model model){
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

    }

    @GetMapping({"/admin_page/page/{page}"})
    public String calculatePage(@AuthenticationPrincipal User user, Model model,
                                @PathVariable Long page) {

         List<OrderDto> orders = orderService.findAllPaidOrdersDTO();
         model.addAttribute("order", orders);

        return "admin/index";
    }

    @GetMapping({"/to_ship"})
    public String shipPage(@AuthenticationPrincipal User user, Model model) {

        List<OrderDto> orders = orderService.findAllPaidOrdersDTO();
        model.addAttribute("order", orders);

        return "admin/index";
    }


    @GetMapping(value = "/to_ship/{id}")
    public String shipOneOrder(@AuthenticationPrincipal User user, Model model, @PathVariable Long id,
                            @PageableDefault Pageable pageable) throws OrderNotFoundException {

        adminService.shipOrder(id);

        return "redirect:/admin/admin_page/page/1";

    }

    @PostMapping(value = "/to_ship")
    public String shipAllOrders(@AuthenticationPrincipal User user, Model model,
                               @PageableDefault Pageable pageable) throws OrderNotFoundException {

        adminService.shipAllOrders();

        return "redirect:/admin/admin_page/page/1";

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

        List<UserDto> users = userService.findAllByLoginLike("%" + userDto.getLogin() + "%");

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


}
