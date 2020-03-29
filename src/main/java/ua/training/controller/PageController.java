package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.exception.RegException;

import ua.training.controller.utility.ControllerUtil;
import ua.training.dto.*;
import ua.training.entity.order.Order;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.OrderRepository;
import ua.training.service.CalculatorService;
import ua.training.service.OrderService;
import ua.training.service.UserService;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {


    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ControllerUtil utility;


    @Autowired
    public PageController(UserService userService, OrderService orderService, CalculatorService calculatorService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @RequestMapping("/")
    public String mainPage(Model model,
                           @RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "reg", required = false) String reg) {


        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("reg", reg != null);

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

    @RequestMapping("/success")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/account_page");
        return redirectView;
    }

    @RequestMapping("/account_page")
    public String accountPage(Model model, @AuthenticationPrincipal User user) {
        insertBalanceInfo(user, model);
        return "account_page";
    }

    @GetMapping("/reg")
    public String registerUser(@ModelAttribute("newUser") UserDTO user,
                               @RequestParam(value = "error", required = false) String error,
                               Model model) {

        model.addAttribute("error", error != null);
        return "reg";
    }

    @PostMapping("/reg")
    public String newUser(@ModelAttribute("newUser") @Valid UserDTO modelUser,
                          BindingResult bindingResult, Model model) throws RegException {

        if (bindingResult.hasErrors()) {
            log.error("registration error");
            model.addAttribute("error", true);
            return "reg";
        } else {
            log.info("saved user  with id=" + modelUser.getId());
            userService.saveNewUser(modelUser);
            return "redirect:/";
        }
    }


    @PostMapping("/neworder")
    public String newOrder(@ModelAttribute OrderDTO modelOrder,
                           @AuthenticationPrincipal User user) throws OrderCreateException {

        orderService.createOrder(modelOrder, user);

        return "redirect:/account_page";
    }

    @GetMapping("/create")
    public String createOrder(@ModelAttribute OrderDTO order, @AuthenticationPrincipal User user,
                              @RequestParam(value = "error", required = false) String error,
                              Model model) {
        insertBalanceInfo(user, model);

        model.addAttribute("error", error != null);
        model.addAttribute("newOrder", order == null ? new OrderDTO() : order);

        return "new_order";
    }


    @RequestMapping("/my_shipments/page/{page}")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user,
                                @PathVariable("page") int page) {

        insertBalanceInfo(user, model);

        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<OrderDTO> articlePage = orderService.findPaginated(user, pageable, isLocaleEn());
        articlePage.getContent().forEach(this::setLocalFields);

        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("orders", articlePage.getContent());

        return "my_shipments";
    }

    @GetMapping("/admin_page")
    public String calculatePage(@AuthenticationPrincipal User user, Model model) {


        insertBalanceInfo(user, model);

        List<OrderDTO> orders = orderService.findAllPaidOrdersDTO();
        orders.forEach(this::setLocalFields);
        model.addAttribute("orders", orders);

        return "admin_page";
    }


    @PostMapping(value = "/admin_page")
    public String adminPage(@AuthenticationPrincipal User user,
                            Model model) throws OrderNotFoundException {

        insertBalanceInfo(user, model);

        List<OrderDTO> orders = orderService.findAllPaidOrdersDTO();
        model.addAttribute("orders", orders);

        for (OrderDTO o : orders) {
            orderService.orderSetShippedStatus(o.getDtoId());
        }

        return "account_page";
    }

    private void setLocalFields(OrderDTO order) {
        utility.reset();
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(LocaleContextHolder.getLocale());

        if (order.getDtoDeliveryDate() != null) {
            order.setDeliveryDate(order.getDtoDeliveryDate().format(pattern));
        }

        if (isLocaleEn()) {
            order.setDtoShippingPrice(order.getDtoShippingPriceEN());
        } else {
            order.setDtoShippingPrice(order.getDtoShippingPrice());
        }

        order.setShippingDate(order.getDtoShippingDate().format(pattern));
        order.setDestination(utility.getMessage(order.getDtoDestination().toString()));
        order.setType(utility.getMessage(order.getDtoOrderType().toString()));
        order.setStatus(utility.getMessage(order.getDtoOrderStatus().getName()));
    }


    private void insertBalanceInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("info", userService.listBankAccountInfo(user.getId(), isLocaleEn()));
    }


    boolean isLocaleEn() {
        return LocaleContextHolder.getLocale().toString().equals("en");
    }

}


