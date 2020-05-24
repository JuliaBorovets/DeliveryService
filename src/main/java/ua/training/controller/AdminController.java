package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.entity.user.User;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping({"/admin_page/page/{page}", "/to_ship", "/"})
    public String calculatePage(@AuthenticationPrincipal User user, Model model,
                                @PathVariable("page") int page) {

//        if (!user.getRole().name().equals("ROLE_ADMIN")) {
//            return "redirect:/account_page";
//        }

        // List<OrderDTO> orders = orderService.findAllPaidOrdersDTO();

        // insertPaginatedOrders(page, ADMIN_SHIPMENTS_SIZE, model, orders);

        return "admin_page";
    }

    @PostMapping(value = "/to_ship")
    public String adminPage(@AuthenticationPrincipal User user, Model model,
                            @PageableDefault Pageable pageable) throws OrderNotFoundException {
        //   orderService.orderToShip();

//        log.info("shipping orders");
        return "redirect:/admin/admin_page/page/1";

    }

}
