package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    @ModelAttribute
    public User loadModelAttribute(@AuthenticationPrincipal User user,  Model model){
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));
        return user;
    }

    @GetMapping({"/admin_page/page/{page}", "/to_ship", "/"})
    public String calculatePage(@AuthenticationPrincipal User user, Model model,
                                @PathVariable("page") int page) {

//        if (!user.getRole().name().equals("ROLE_ADMIN")) {
//            return "redirect:/account_page";
//        }

        // List<OrderDTO> orders = orderService.findAllPaidOrdersDTO();

        return "admin_page";
    }

    @PostMapping(value = "/to_ship")
    public String adminPage(@AuthenticationPrincipal User user, Model model,
                            @PageableDefault Pageable pageable) throws OrderNotFoundException {

        //   orderService.orderToShip();

        return "redirect:/admin/admin_page/page/1";

    }

}
