package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.training.dto.LanguageDTO;
import ua.training.dto.OrderDTO;
import ua.training.dto.OrdersDTO;
import ua.training.dto.UserDTO;
import ua.training.entity.order.Order;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class PageController implements WebMvcConfigurer {

    private final UserService userService;
    private final OrderService orderService;
    private LanguageDTO languageChanger = new LanguageDTO();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PageController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @RequestMapping("/")
    public String mainPage(@RequestParam(value = "reg", required = false) String reg,
                           @RequestParam(value = "login", required = false) String login,
                           Model model) {

        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        model.addAttribute("language", languageChanger);
        model.addAttribute("supported", languageChanger.getSupportedLanguages());

        model.addAttribute("reg", reg != null);
        model.addAttribute("login", login != null);
        return "index.html";
    }


    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "reg", required = false) String reg,
                            Model model) {

        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        model.addAttribute("language", languageChanger);
        model.addAttribute("supported", languageChanger.getSupportedLanguages());

        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("reg", reg != null);

        return "login.html";
    }


    @RequestMapping("/logout_new")
    public String logoutPage(@RequestParam(value = "reg", required = false) String reg,
                             @RequestParam(value = "login", required = false) String login,
                             Model model) {

        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        model.addAttribute("language", languageChanger);
        model.addAttribute("supported", languageChanger.getSupportedLanguages());

        model.addAttribute("reg", reg != null);
        model.addAttribute("login", login != null);
        return "index.html";
    }

    @RequestMapping("/success")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/account_page");
        return redirectView;
    }


    @RequestMapping("/account_page")
    public String accountPage(Model model) {
        model.addAttribute("user_role_admin", currentUserRoleAdmin());
        model.addAttribute("language", languageChanger);
        model.addAttribute("error", false);
        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        return "account_page.html";
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

        return "reg.html";
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

        return "new_order.html";
    }

    @RequestMapping("/calc")
    public String calculatorPage(Model model) {
        model.addAttribute("language", languageChanger);
        model.addAttribute("user", getCurrentUser());
        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        return "calculator.html";
    }

    @RequestMapping("/my_shipments")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("language", languageChanger);
        model.addAttribute("user_role_admin", currentUserRoleAdmin());
        List<Order> orders = orderService.findAllOrders(user.getId());
        model.addAttribute("orders", orders);
        languageChanger.setChoice(LocaleContextHolder.getLocale().toString());
        return "my_shipments.html";
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


//    private List<Order> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders().getOrders();
//        return orders;
//    }


    private void changeToCyrillic(UserDTO user) {
        if (languageChanger.getChoice().equals(SupportedLanguages.UKRAINIAN.getCode())) {
            user.setFirstName(user.getFirstNameCyr());
            user.setLastName(user.getLastNameCyr());
        } else {
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
        }
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

