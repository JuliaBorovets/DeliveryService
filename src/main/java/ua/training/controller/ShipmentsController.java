package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.DestinationDto;
import ua.training.dto.OrderDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.DestinationService;
import ua.training.service.OrderService;
import ua.training.service.OrderTypeService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    public void setModel(@AuthenticationPrincipal User user,  Model model){

        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

    }

    @GetMapping("/show/{page}/{filter}")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user,
                                @PathVariable("page") Integer page, @PathVariable String filter,
                                @RequestParam(required = false) String error) {

        model.addAttribute("error", error != null);
        model.addAttribute("orderDto", OrderDto.builder().build());

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

        return "user/my_shipments";
    }

    @GetMapping("/find_order")
    public String findOrderById(@ModelAttribute OrderDto orderDto,
                                Model model) throws OrderNotFoundException {

        List<OrderDto> order = Collections.singletonList(orderService.getOrderDtoById(orderDto.getId()));

        model.addAttribute("orders", order);

        return "user/my_shipments";
    }

    @GetMapping("/create_shipment")
    public String createOrderView( Model model) {

        OrderDto orderDto = OrderDto.builder().build();

        List<DestinationDto> destinationDto = destinationService.getAllDestinationDto();

        Set<String> destinationsFrom = destinationDto.stream()
                .map(DestinationDto::getCityFrom)
                .collect(Collectors.toSet());

        Set<String> destinationsTo = destinationDto.stream()
                .map(DestinationDto::getCityTo)
                .collect(Collectors.toSet());

        model.addAttribute("newOrder", orderDto);
        model.addAttribute("types", orderTypeService.getAllOrderTypeDto());
        model.addAttribute("destinationsFrom", destinationsFrom);
        model.addAttribute("destinationsTo", destinationsTo);

        return "user/new_order";
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
