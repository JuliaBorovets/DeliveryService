package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.training.controller.exception.RegException;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
public class PageController implements WebMvcConfigurer {

    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public User loadModelAttribute(@AuthenticationPrincipal User user,  Model model){

        model.addAttribute("user", user);

        return user;
    }
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/reg")
    public String registerUser(Model model) {

        model.addAttribute("newUser", UserDto.builder().build());

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
    public String accountPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

        return "user/account_page";
    }

    @RequestMapping("/success")
    public String localRedirect() {

        return "redirect:/account_page";
    }

    @ExceptionHandler(RegException.class)
    public String handleRegException(Model model) {
        log.error("registration exception. Duplicate user.");
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("duplicate", true);
        return "registration";
    }
}


