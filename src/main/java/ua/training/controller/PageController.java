package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.training.dao.BankAccountDAO;

import ua.training.dto.*;
import ua.training.entity.order.Order;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.CalculatorService;
import ua.training.service.OrderService;
import ua.training.service.UserService;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {

    @Autowired
    private BankAccountDAO bankAccountDAO;

    private final UserService userService;
    private final OrderService orderService;
    private final CalculatorService calculatorService;
    private LanguageDTO languageChanger = new LanguageDTO();

    @Autowired
    private PasswordEncoder passwordEncoder;

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


        insertLang(model);
        model.addAttribute("reg", reg != null);
        model.addAttribute("login", login != null);
        return "index";
    }


    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "reg", required = false) String reg,
                            Model model) {

        insertLang(model);

        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("reg", reg != null);

        return "login";
    }


    @RequestMapping("/logout_new")
    public String logoutPage(@RequestParam(value = "reg", required = false) String reg,
                             @RequestParam(value = "login", required = false) String login,
                             Model model) {

        insertLang(model);

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
    public String accountPage(Model model) {

        insertLang(model);

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
    public RedirectView newUser(@ModelAttribute User modelUser, RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView();
        if (!verifyUserFields(modelUser)) {
            redirectAttributes.addFlashAttribute("user", modelUser);
            redirectView.setUrl("/reg?error");
            return redirectView;
        }

        try {
            userService.saveNewUser(modelUser);
        } catch (RegException e) {
            redirectAttributes.addFlashAttribute("user", modelUser);
            redirectView.setUrl(e.isDuplicate() ? "/reg?duplicate" : "/reg?error");
            return redirectView;
        }

        redirectView.setUrl("/?reg");
        return redirectView;
    }

    @RequestMapping("/neworder")
    public RedirectView newOrder(@ModelAttribute OrderDTO modelOrder,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView();
        if (!verifyOrderFields(modelOrder)) {
            redirectAttributes.addFlashAttribute("order", modelOrder);
            redirectView.setUrl("/create?error");
            return redirectView;
        }

        try {
            orderService.createOrder(modelOrder, user);
        } catch (Exception e) {
            e.printStackTrace();
            return redirectView;
        }

        redirectView.setUrl("/account_page");
        return redirectView;
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
        insertLang(model);

        model.addAttribute("user_role_admin", currentUserRoleAdmin());
        List<Order> orders = orderService.findAllOrders(user.getId());
        model.addAttribute("orders", orders);

        return "my_shipments";
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
    public String processSendMoney(Model model, BankAccountDTO sendMoneyForm,
                                   Order order) {

        model.addAttribute("sendMoneyForm", sendMoneyForm);


        try {
            bankAccountDAO.sendMoney(sendMoneyForm.getFromAccountId(), //
                    sendMoneyForm.getToAccountId(), //
                    sendMoneyForm.getAmount());
            orderService.payForOrder(orderService.getOrderById(1L));
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
//
//
//       // log.error(modelUser.getId().toString());
//        model.addAttribute("addMoneyForm", addMoneyForm);

        try {
            bankAccountDAO.addAmount(user.getId(), addMoneyForm.getAmount());

        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/adding_money";
        }
        return "redirect:/account_page";
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
        UserDTO currentUser = getCurrentUser();
        return currentUser.getRoleType() == RoleType.ROLE_ADMIN;
    }


    private UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO currentUser;

        try {
            currentUser = (UserDTO) auth.getPrincipal();
        } catch (ClassCastException e) {
            return new UserDTO();
        }

        changeToCyrillic(currentUser);

        return currentUser;
    }


    private void changeToCyrillic(UserDTO user) {
        if (languageChanger.getChoice().equals(SupportedLanguages.UKRAINIAN.getCode())) {
            user.setFirstName(user.getFirstNameCyr());
            user.setLastName(user.getLastNameCyr());
        } else {
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
        }
    }

    public void insertLang(Model model) {
        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        model.addAttribute("language", languageChanger);
        model.addAttribute("supported", languageChanger.getSupportedLanguages());
        model.addAttribute("supported", languageChanger.getSupportedLanguages());

    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}


