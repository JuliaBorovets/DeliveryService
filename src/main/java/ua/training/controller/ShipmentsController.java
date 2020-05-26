package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderCreateException;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;
import ua.training.service.OrderService;
import ua.training.service.serviceImpl.OrderServiceImpl;


@Slf4j
@RequestMapping("/shipments")
@Controller
public class ShipmentsController {

    private final OrderService orderService;

    public ShipmentsController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/show/{page}")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user,
                                @PathVariable("page") Integer page) {


//        List<OrderDTO> orders = orderService.findAllUserOrder(user.getId());
//
//        insertPaginatedOrders(page, USER_SHIPMENTS_SIZE, model, orders);

        return "my_shipments";
    }

    @GetMapping("/create_order")
    public String createOrderView( Model model) {

        model.addAttribute("newOrder", new OrderDto());

        return "new_order";
    }

    @PostMapping("/create_order")
    public String createOrder(@ModelAttribute OrderDto modelOrder, @AuthenticationPrincipal UserDto userDto) throws OrderCreateException {

        //orderService.createOrder(modelOrder, user);

        return "redirect:/shipments/show/1";

    }

}
