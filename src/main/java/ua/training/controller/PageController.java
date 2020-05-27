package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.training.controller.exception.RegException;
import ua.training.dto.UserDto;
import ua.training.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {

    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/reg")
    public String registerUser(Model model) {

        model.addAttribute("newUser", new UserDto());

        return "registration";
    }

    @PostMapping("/reg")
    public String newUser(@Valid @ModelAttribute("newUser") UserDto modelUser) throws RegException {

        userService.saveNewUserDto(modelUser);
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


    @PostMapping("/success")
    public String localRedirect() {

        return "redirect:/account_page";
    }


}


