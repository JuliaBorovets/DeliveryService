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
import ua.training.dto.StatisticsDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.AdminService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    public User loadModelAttribute(@AuthenticationPrincipal User user,  Model model){
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));
        return user;
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

        StatisticsDto statisticsDto = adminService.createStatisticsDto();
        model.addAttribute("statistics", statisticsDto);

        Map<Integer, Long> orders = adminService.statisticsNumberOfOrdersByForYear(2020);
        model.addAttribute("orders", orders);

        Map<Integer, BigDecimal> earnings = adminService.statisticsEarningsOfOrdersByForYear(2020);

        model.addAttribute("earnings", earnings);

        return "admin/statistics";
    }


    @GetMapping("/change_roles")
    public String changeRoles(Model model){

        model.addAttribute("users", userService.findAllUserDto());
        model.addAttribute("roles", RoleType.values());
        
        return "admin/change_roles";

    }

}
