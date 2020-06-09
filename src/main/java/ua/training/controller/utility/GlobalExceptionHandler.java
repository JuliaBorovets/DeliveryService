package ua.training.controller.utility;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.training.controller.exception.*;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleApplicationException() {
//        log.error("global exception");
//        ModelAndView modelAndView = new ModelAndView("index");
//        modelAndView.addObject("error", true);
//        return modelAndView;
//    }


//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleNotFound(Exception exception){
//        log.error("not found exception");
//        log.error(exception.getMessage());
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("404");
//        modelAndView.addObject("exception", exception);
//        return modelAndView;
//    }


//    @ExceptionHandler({org.springframework.validation.BindException.class, IllegalStateException.class})
//    public String handleApplicationException(Model model) {
//        log.error("registration exception. Binding result.");
//        model.addAttribute("newUser", new UserDto());
//        model.addAttribute("error", true);
//        return "registration";
//    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException(Model model) {
        log.error("OrderNotFound Exception");
        model.addAttribute("error", true);
        return "redirect:/shipments/show/1/all";
    }

    @ExceptionHandler(OrderCheckException.class)
    public String handleOrderCheckException(Model model) {
        log.error("OrderNotFound Exception");
        model.addAttribute("error", true);
        return "redirect:/admin/show_checks";
    }

    @ExceptionHandler(RegException.class)
    public String handleRegException(Model model) {
        log.error("registration exception. Duplicate user.");
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("duplicate", true);
        return "registration";
    }

    @ExceptionHandler(BankTransactionException.class)
    public String handleRegException() {
        log.error("BankTransaction Exception");
        return "redirect:/adding_money";
    }


    @ExceptionHandler(OrderCreateException.class)
    public String handleOrderCreateException(Model model) {
        log.error("OrderCreate exception");
        model.addAttribute("newOrder", new OrderDto());
        model.addAttribute("error", true);
        return "redirect:/create?error";
    }

}
