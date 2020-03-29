package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.training.controller.exception.BankTransactionException;

import ua.training.dto.*;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.user.User;
import ua.training.service.CalculatorService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Controller
public class PaymentController {

    private final OrderService orderService;
    private final UserService userService;
    private final CalculatorService calculatorService;

    public PaymentController(OrderService orderService, UserService userService, CalculatorService calculatorService) {
        this.orderService = orderService;
        this.userService = userService;
        this.calculatorService = calculatorService;
    }

    @GetMapping("/calculator")
    public String calculatePage(@ModelAttribute OrderDTO modelOrder,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {

        model.addAttribute("error", false);


        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculatePrice(@ModelAttribute("order") @Valid CalculatorDTO order,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "error", required = false) String error,
                                 @ModelAttribute User modelUser, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            return "calculator";
        } else {
            model.addAttribute("price", calculatorService.calculatePrice(order));
        }

        return "calculator";
    }

    @RequestMapping(value = "/to_pay", method = RequestMethod.GET)
    public String viewSendMoneyPage(@AuthenticationPrincipal User user, Model model) {
        insertBalanceInfo(user, model);
        return "payment";
    }


    @RequestMapping("pay/{id}")
    String payShipment(@PathVariable("id") long shipmentId) {

        try {
            Order order = orderService.getOrderById(shipmentId);
            orderService.payForOrder(order);

        } catch (BankTransactionException e) {
            return "redirect:/my_shipments/page/1";
        }

        return "redirect:/my_shipments/page/1";
    }

    @RequestMapping(value = "/add_money", method = RequestMethod.GET)
    public String addMoneyPage(@AuthenticationPrincipal User user, Model model) {
        insertBalanceInfo(user, model);
        return "adding_money";
    }


    @RequestMapping(value = "/add_money", method = RequestMethod.POST)
    public String addMoney(Model model, @ModelAttribute("add") AddMoneyDTO addMoneyForm, @AuthenticationPrincipal User user) {
        log.error(user.getBalance().toString());
        try {
            orderService.addAmount(user.getId(), addMoneyForm.getAmount());

        } catch (BankTransactionException e) {
            return "/adding_money";
        }
        return "redirect:/account_page";
    }

    private void insertBalanceInfo(@AuthenticationPrincipal User user, Model model) {
        log.error(userService.listBankAccountInfo(user.getId()).toString());
        model.addAttribute("info", userService.listBankAccountInfo(user.getId()));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleApplicationException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("error", true);
        return modelAndView;
    }
}
