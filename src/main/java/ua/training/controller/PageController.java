package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;
import ua.training.controller.exception.RegException;

import ua.training.controller.utility.ControllerUtil;
import ua.training.dto.*;
import ua.training.entity.order.Order;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.CalculatorService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {


    private final UserService userService;
    private final OrderService orderService;
    private final CalculatorService calculatorService;

    @Autowired
    private ControllerUtil utility;


    @Autowired
    public PageController(UserService userService, OrderService orderService, CalculatorService calculatorService) {
        this.userService = userService;
        this.orderService = orderService;
        this.calculatorService = calculatorService;
    }

    @RequestMapping("/")
    public String mainPage(@RequestParam(value = "reg", required = false) String reg,
                           @RequestParam(value = "login", required = false) String login,
                           Model model) {

        model.addAttribute("reg", reg != null);
        model.addAttribute("login", login != null);
        return "index";
    }


    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "reg", required = false) String reg,
                            Model model) {

        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("reg", reg != null);

        return "login";
    }


    @RequestMapping("/logout_new")
    public String logoutPage(@RequestParam(value = "reg", required = false) String reg,
                             @RequestParam(value = "login", required = false) String login,
                             Model model) {

        model.addAttribute("reg", reg != null);
        model.addAttribute("login", login != null);
        return "index";
    }

    @RequestMapping("/success")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/account_page");
        return redirectView;
    }


    @RequestMapping("/account_page")
    public String accountPage(Model model, @AuthenticationPrincipal User user) {
        //insertBalanceInfo(user, model);
        model.addAttribute("error", false);
        return "account_page";
    }

    @RequestMapping("/reg")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "duplicate", required = false) String duplicate,
                               Model model) {

        model.addAttribute("firstNameRegex", "^" + RegistrationValidation.FIRST_NAME_REGEX + "$");
        model.addAttribute("firstNameCyrRegex", "^" + RegistrationValidation.FIRST_NAME_CYR_REGEX + "$");
        model.addAttribute("lastNameRegex", "^" + RegistrationValidation.LAST_NAME_REGEX + "$");
        model.addAttribute("lastNameCyrRegex", "^" + RegistrationValidation.LAST_NAME_CYR_REGEX + "$");
        model.addAttribute("loginRegex", "^" + RegistrationValidation.LOGIN_REGEX + "$");

        model.addAttribute("error", error != null);
        model.addAttribute("duplicate", duplicate != null);
        model.addAttribute("newUser", user == null ? new User() : user);

        return "reg";
    }

    @RequestMapping("/newuser")
    public String newUser(@ModelAttribute User modelUser, Model model) {

        if (!verifyUserFields(modelUser)) {
            model.addAttribute("error", true);
            return "redirect:/reg?error";
        }

        try {
            userService.saveNewUser(modelUser);
        } catch (RegException e) {
            return "redirect:/reg?error";
        }

        return "redirect:/";
    }

    @RequestMapping("/neworder")
    public String newOrder(@ModelAttribute OrderDTO modelOrder,
                           @AuthenticationPrincipal User user) {

        if (!verifyOrderFields(modelOrder)) {
            return "redirect:/create?error";
        }

        try {
            orderService.createOrder(modelOrder, user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/create?error";
        }

        return "redirect:/account_page";
    }


    @RequestMapping("/create")
    public String createOrder(@ModelAttribute OrderDTO order,
                              @RequestParam(value = "error", required = false) String error,
                              Model model) {

        model.addAttribute("error", error != null);
        model.addAttribute("newOrder", order == null ? new User() : order);

        return "new_order";
    }


    @RequestMapping("/my_shipments")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user) {
        //insertBalanceInfo(user, model);
        List<OrderDTO> orders = orderService.orderDTOList(user.getId());
        orders.forEach(this::setLocalFields);

        model.addAttribute("orders", orders);

        return "my_shipments";
    }

    @GetMapping("/admin_page")
    public String calculatePage(@AuthenticationPrincipal User user, Model model) {

        //insertBalanceInfo(user, model);

        if (!currentUserRoleAdmin()) {
            return "account_page";
        }
        model.addAttribute("admin", currentUserRoleAdmin());
        List<Order> orders = orderService.findAllPaidOrders();
        model.addAttribute("orders", orders);

        return "admin_page";
    }


    @PostMapping("/admin_page")
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        log.error(String.valueOf(user.getRole().equals(RoleType.ROLE_ADMIN)));
        //insertBalanceInfo(user, model);
        if (!currentUserRoleAdmin()) {
            return "account_page";
        }
        model.addAttribute("admin", currentUserRoleAdmin());
        List<Order> orders = orderService.findAllPaidOrders();
        model.addAttribute("orders", orders);

        for (Order o : orders) {
            orderService.orderSetShippedStatus(o);
        }

        return "admin_page";


    }


    private boolean verifyUserFields(User user) {
        return user.getFirstName().matches(RegistrationValidation.FIRST_NAME_REGEX) &&
                user.getFirstNameCyr().matches(RegistrationValidation.FIRST_NAME_CYR_REGEX) &&
                user.getLastName().matches(RegistrationValidation.LAST_NAME_REGEX) &&
                user.getLastNameCyr().matches(RegistrationValidation.LAST_NAME_CYR_REGEX) &&
                user.getLogin().matches(RegistrationValidation.LOGIN_REGEX);
    }

    private boolean verifyOrderFields(OrderDTO order) {
        return order.getDtoDescription().matches(OrderValidation.DESCRIPTION);
    }


    private boolean currentUserRoleAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getRole().equals(RoleType.ROLE_ADMIN);
    }

//    private void insertBalanceInfo(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("info", userService.listBankAccountInfo(user));
//    }

    private void setLocalFields(OrderDTO order) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(LocaleContextHolder.getLocale());
        utility.reset();
        // order.setDeliveryDate(order.getDtoDeliveryDate().format(timeFormatter));
        order.setDestination(utility.getMessage(order.getDtoDestination().getName()));
        order.setType(utility.getMessage(order.getDtoOrderType().getName()));
        order.setStatus(utility.getMessage(order.getOrderStatus().getName()));
    }

}


