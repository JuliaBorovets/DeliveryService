package ua.training.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.exception.RegException;
import ua.training.dto.OrderDTO;
import ua.training.dto.UserDTO;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleApplicationException() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("error", true);
        return modelAndView;
    }


    @ExceptionHandler({org.springframework.validation.BindException.class, IllegalStateException.class})
    public String handleApplicationException(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("error", true);
        return "registration";
    }

    @ExceptionHandler(BankTransactionException.class)
    public String handleRegException() {
        log.error("BankTransaction Exception");
        return "redirect:/adding_money";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException() {
        log.error("OrderNotFound Exception");
        return "redirect:/my_shipments/page/1";
    }


    @ExceptionHandler(RegException.class)
    public String handleRegException(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("duplicate", true);
        return "reg";
    }

    @ExceptionHandler(OrderCreateException.class)
    public String handleOrderCreateException(Model model) {
        model.addAttribute("newOrder", new OrderDTO());
        model.addAttribute("error", true);
        return "redirect:/create?error";
    }

}
