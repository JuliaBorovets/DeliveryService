package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.*;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.user.User;
import ua.training.service.CalculatorService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@Controller
public class PaymentController {

    private final OrderService orderService;
    private final CalculatorService calculatorService;

    public PaymentController(OrderService orderService, CalculatorService calculatorService) {
        this.orderService = orderService;
        this.calculatorService = calculatorService;
    }


    @PostMapping("/calculator")
    public String calculatePrice(@ModelAttribute("order") @Valid CalculatorDTO order,
                                 @ModelAttribute User modelUser, Model model) {

        model.addAttribute("price", calculatorService.calculatePrice(order));
        return "calculator";
    }

    @RequestMapping(value = "pay/{id}")
    public String payShipment(@PathVariable("id") long shipmentId) throws BankTransactionException, OrderNotFoundException {

        Order order = orderService.getOrderById(shipmentId);

        if (!order.getOrderStatus().equals(OrderStatus.PAID) && !order.getOrderStatus().equals(OrderStatus.SHIPPED))
            orderService.payForOrder(order);

        return "redirect:/my_shipments/page/1";
    }


    @PostMapping(value = "/add_money")
    public String addMoney(Model model, @ModelAttribute("add") AddMoneyDTO addMoneyForm,
                           @AuthenticationPrincipal User user) throws BankTransactionException {

        orderService.addAmount(user.getId(), addMoneyForm.getAmount());

        return "redirect:/adding_money";

    }

}
