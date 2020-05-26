package ua.training.controller.utility;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.exception.RegException;
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


    @ExceptionHandler({org.springframework.validation.BindException.class, IllegalStateException.class})
    public String handleApplicationException(Model model) {
        log.error("registration exception. Binding result.");
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("error", true);
        return "registration";
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

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException() {
        log.error("OrderNotFound Exception");
        return "redirect:/my_shipments/page/1";
    }


    @ExceptionHandler(OrderCreateException.class)
    public String handleOrderCreateException(Model model) {
        log.error("OrderCreate exception");
        model.addAttribute("newOrder", new OrderDto());
        model.addAttribute("error", true);
        return "redirect:/create?error";
    }

}
