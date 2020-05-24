package ua.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.training.controller.exception.RegException;
import ua.training.dto.*;

import lombok.extern.slf4j.Slf4j;
import ua.training.service.serviceImpl.UserServiceImpl;

import javax.validation.Valid;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {

    private final UserServiceImpl userService;

    public PageController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/reg")
    public String registerUser(@ModelAttribute("newUser") UserDTO user, Model model) {

        model.addAttribute("newOrder", user == null ? new UserDTO() : user);

        return "registration";
    }

    @PostMapping("/reg")
    public String newUser(@ModelAttribute("newUser") @Valid UserDTO modelUser) throws RegException {

        userService.saveNewUser(modelUser);
        log.info("new user registration");

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/account_page")
    public String accountPage() {
        return "account_page";
    }

    @RequestMapping("/success")
    public String localRedirect() {
        return "redirect:/account_page";
    }

    @GetMapping("/calculator")
    public String calculatePage(@ModelAttribute OrderDTO modelOrder) {
        return "calculator";
    }

}


