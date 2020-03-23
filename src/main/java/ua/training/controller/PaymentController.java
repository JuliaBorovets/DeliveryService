package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.dao.BankAccountDAO;
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

    @Autowired
    private BankAccountDAO bankAccountDAO;

    private final UserService userService;
    private final OrderService orderService;
    private final CalculatorService calculatorService;
    private LanguageDTO languageChanger = new LanguageDTO();

    public PaymentController(UserService userService, OrderService orderService, CalculatorService calculatorService) {
        this.userService = userService;
        this.orderService = orderService;
        this.calculatorService = calculatorService;
    }

    @GetMapping("/calculator")
    public String calculatePage(@ModelAttribute OrderDTO modelOrder,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {

        insertLang(model);
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
        insertLang(model);
        model.addAttribute("error", false);
        model.addAttribute("price", calculatorService.calculatePrice(order));
        return "calculator";
    }

    //TODO checking fields

    @RequestMapping(value = "/to_pay", method = RequestMethod.GET)
    public String viewSendMoneyPage(Model model) {
        return "payment";
    }


    @RequestMapping(value = "/to_pay", method = RequestMethod.POST)
    public String processSendMoney(Model model, OrderPayDTO orderPayDTO, @AuthenticationPrincipal User user,
                                   Order order) {

        model.addAttribute("sendMoneyForm", orderPayDTO);

        Long ownerAccount = 1L;


        try {

            BigDecimal amount = orderService.getOrderById(orderPayDTO.getOrderNumber()).getShippingPrice();
            bankAccountDAO.sendMoney(user.getId(),
                    ownerAccount, amount);
            orderService.payForOrder(orderService.getOrderById(orderPayDTO.getOrderNumber()));
        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/payment";
        }
        return "redirect:/account_page";
    }

    @RequestMapping(value = "/add_money", method = RequestMethod.GET)
    public String addMoneyPage(Model model) {
        return "adding_money";
    }


    @RequestMapping(value = "/add_money", method = RequestMethod.POST)
    public String addMoney(Model model, @ModelAttribute("add") AddMoneyDTO addMoneyForm, @AuthenticationPrincipal User user,
                           Order order) {

        try {
            bankAccountDAO.addAmount(user.getId(), addMoneyForm.getAmount());

        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/adding_money";
        }
        return "redirect:/account_page";
    }

    public void insertLang(Model model) {
        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        model.addAttribute("language", languageChanger);
        model.addAttribute("supported", languageChanger.getSupportedLanguages());
        model.addAttribute("supported", languageChanger.getSupportedLanguages());

    }
}
