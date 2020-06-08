package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.DestinationService;
import ua.training.service.OrderService;
import ua.training.service.OrderTypeService;


@Slf4j
@RequestMapping("/shipments")
@Controller
public class ShipmentsController {

    private final OrderService orderService;
    private final OrderTypeService orderTypeService;
    private final DestinationService destinationService;

    public ShipmentsController(OrderService orderService, OrderTypeService orderTypeService,
                               DestinationService destinationService) {
        this.orderService = orderService;
        this.orderTypeService = orderTypeService;
        this.destinationService = destinationService;

    }

    @ModelAttribute
    public User loadPetWithVisit(@AuthenticationPrincipal User user,  Model model){

        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

        return user;
    }

    @GetMapping("/show/{page}/{filter}")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user,
                                @PathVariable("page") Integer page, @PathVariable String filter) {


        switch (filter){
            case "all":
                model.addAttribute("orders", orderService.findAllUserOrders(user.getId()));
                break;
            case "paid" :
                model.addAttribute("orders", orderService.findAllPaidUserOrders(user.getId()));
                break;
            case "not_paid":
                model.addAttribute("orders", orderService.findAllNotPaidUserOrders(user.getId()));
                break;
            case "shipped":
                model.addAttribute("orders", orderService.findAllShippedUserOrders(user.getId()));
                break;
            case "archived":
                model.addAttribute("orders", orderService.findAllArchivedUserOrders(user.getId()));
                break;
        }

        return "my_shipments";
    }

    @GetMapping("/create_shipment")
    public String createOrderView( Model model) {

        OrderDto orderDto = OrderDto.builder().build();

        model.addAttribute("newOrder", orderDto);
        model.addAttribute("types", orderTypeService.getAllOrderTypeDto());
        model.addAttribute("destinations", destinationService.getAllDestinationDto());

        return "new_order";
    }

    @PostMapping("/create_shipment")
    public String createOrder(@ModelAttribute OrderDto modelOrder, @AuthenticationPrincipal User user)
            throws OrderCreateException {

        orderService.createOrder(modelOrder, user);

        return "redirect:/shipments/show/1/all";
    }


    @GetMapping("/archive_order/{orderId}")
    public String getMoveToArchive(@PathVariable Long orderId) throws OrderNotFoundException {

        orderService.moveOrderToArchive(orderId);

        log.info("moving to archive");

        return "redirect:/shipments/show/1/all";
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) throws OrderNotFoundException {

        orderService.deleteOrderById(orderId);
        log.info("deleting order with id = " + orderId);
        return "redirect:/shipments/show/1/all";

    }
}
