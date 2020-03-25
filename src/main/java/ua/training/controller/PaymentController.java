package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.BankTransactionException;

import ua.training.dto.*;
import ua.training.entity.order.Order;
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

    @GetMapping("/calculator")
    public String calculatePage(@ModelAttribute OrderDTO modelOrder,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {

        model.addAttribute("error", false);

        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculatePrice(@ModelAttribute("order") @Valid CalculatorDTO order,
                                 @ModelAttribute User modelUser,
                                 @RequestParam(value = "error", required = false) String error,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "calculator";
        }
        model.addAttribute("error", false);
        model.addAttribute("price", calculatorService.calculatePrice(order));
        return "calculator";
    }

    @RequestMapping(value = "/to_pay", method = RequestMethod.GET)
    public String viewSendMoneyPage(@AuthenticationPrincipal User user, Model model) {

        return "payment";
    }


    @RequestMapping(value = "/to_pay", method = RequestMethod.POST)
    public String processSendMoney(Model model, OrderPayDTO orderPayDTO, @AuthenticationPrincipal User user) {


        Long ownerAccount = 1L;
        model.addAttribute("sendMoneyForm", orderPayDTO);

        try {
            Order order = orderService.getOrderById(orderPayDTO.getOrderNumber());
            if (orderService.isPaid(order) || orderService.isShipped(order)) {
                throw new BankTransactionException("order is already paid");
            }
            BigDecimal amount = order.getShippingPrice();
            orderService.sendMoney(user.getId(), ownerAccount, amount);
            orderService.payForOrder(orderService.getOrderById(orderPayDTO.getOrderNumber()));
        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/payment";
        }
        return "redirect:/my_shipments";
    }

    @RequestMapping(value = "/add_money", method = RequestMethod.GET)
    public String addMoneyPage(@AuthenticationPrincipal User user, Model model) {
        return "adding_money";
    }


    @RequestMapping(value = "/add_money", method = RequestMethod.POST)
    public String addMoney(Model model, @ModelAttribute("add") AddMoneyDTO addMoneyForm, @AuthenticationPrincipal User user,
                           Order order) {
        log.info(orderService.listBankAccountInfo(user).toString());
        try {
            orderService.addAmount(user.getId(), addMoneyForm.getAmount());

        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/adding_money";
        }
        return "redirect:/account_page";
    }

}
