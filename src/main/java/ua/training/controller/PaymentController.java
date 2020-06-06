package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.training.dto.CalculatorDto;
import ua.training.dto.OrderDto;
import ua.training.entity.user.User;
import ua.training.service.serviceImpl.OrderServiceImpl;

import javax.validation.Valid;

@Slf4j
@Controller
public class PaymentController {

    private final OrderServiceImpl orderService;

    public PaymentController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/calculator")
    public String calculatePage(@ModelAttribute OrderDto modelOrder) {
        return "calculator";
    }

    @PostMapping ("/calculator")
    public String calculatePrice(@ModelAttribute("order") @Valid CalculatorDto order,
                                 @ModelAttribute User modelUser, Model model) {

       // model.addAttribute("price", calculatorService.calculatePrice(order));
        log.info("calculating order price");
        return "calculator";
    }

//    @RequestMapping(value = "pay/{id}")
//    public String payShipment(@PathVariable("id") long shipmentId) throws BankTransactionException, OrderNotFoundException {
//
//       // Order order = orderService.getOrderById(shipmentId);
//
////        if (!order.getOrderStatus().equals(OrderStatus.PAID) && !order.getOrderStatus().equals(OrderStatus.SHIPPED))
////            orderService.payForOrder(order);
//        log.info("order paying");
//
//        return "redirect:/my_shipments/page/1";
//    }




}
