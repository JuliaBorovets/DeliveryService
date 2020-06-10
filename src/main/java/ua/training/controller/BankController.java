package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.CanNotPayException;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.BankCardService;
import ua.training.service.OrderCheckService;
import ua.training.service.UserService;
import ua.training.service.serviceImpl.OrderServiceImpl;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Slf4j
@RequestMapping("/bank")
@Controller
public class BankController {

    private final OrderServiceImpl orderService;
    private final BankCardService bankCardService;
    private final OrderCheckService orderCheckService;
    private final UserService userService;

    public BankController(OrderServiceImpl orderService, BankCardService bankCardService,
                          OrderCheckService orderCheckService, UserService userService) {
        this.orderService = orderService;
        this.bankCardService = bankCardService;
        this.orderCheckService = orderCheckService;
        this.userService = userService;
    }

    @ModelAttribute
    public void setModel(@AuthenticationPrincipal User user,  Model model, @RequestParam(required = false) String error){

        model.addAttribute("error", error != null);
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

    }

    @GetMapping
    public String getBankInfo(@AuthenticationPrincipal User user, @RequestParam(required = false) String canNotPay,
                              Model model){

        model.addAttribute("canNotPay", canNotPay != null);
        model.addAttribute("bankCards", bankCardService.getAllUserBankCards(user));

        return "bank/info";
    }


    @GetMapping("/add_card")
    public String getBankPage( Model model) {

        model.addAttribute("bankDTO", BankCardDto.builder().build());

        return "bank/bank_card_add";
    }


    @PostMapping("/add_card")
    public String addBankCard(@Valid @ModelAttribute("bankDTO") BankCardDto bankCardDTO, BindingResult bindingResult,
                              @AuthenticationPrincipal User user)
            throws BankException {

        if (bindingResult.hasErrors()){
            return "bank/bank_card_add";
        }

        String expDate = (bankCardDTO.getExpMonth() < 10) ?
                ("01/0" + bankCardDTO.getExpMonth() + "/" + bankCardDTO.getExpYear()) :
                ("01/" + bankCardDTO.getExpMonth() + "/" + bankCardDTO.getExpYear());

        if (LocalDate.parse(expDate, DateTimeFormatter.ofPattern("dd/MM/yyy")).isBefore(LocalDate.now())) {

            bindingResult.rejectValue("expYear", "not valid", "Not valid expiration date");

        }

        bankCardService.saveBankCardDTO(bankCardDTO, user.getId());

        log.error("adding card");

        return "redirect:/bank";
    }

    @GetMapping("/update_card/{cardId}")
    public String getUpdateBankPage(@PathVariable Long cardId, Model model) throws BankException {

        model.addAttribute("bankDTO", bankCardService.findBankCardDtoById(cardId));

        return "bank/bank_card_update";
    }

    @PostMapping("/update_card/{cardId}")
    public String updateBankCard(@PathVariable Long cardId, @ModelAttribute BankCardDto bankCardDTO) throws BankException {

        bankCardService.updateBankCardDTO(bankCardDTO, cardId);

        log.error("updating card");

        return "redirect:/bank";
    }


    @GetMapping("/delete_card/{cardId}")
    public String deleteBankCard(@PathVariable Long cardId, @AuthenticationPrincipal User user) throws BankException {

        log.error("deleting card");
        bankCardService.deleteBankCardConnectionWithUser(cardId, user.getId());

        return "redirect:/bank";
    }

    @GetMapping(value = "/pay/{orderId}")
    public String payParticularShipmentView(@PathVariable Long orderId, Model model, @AuthenticationPrincipal User user,
                                            @ModelAttribute("checkDto") OrderCheckDto orderCheckDto,
                                            @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws OrderNotFoundException {

        model.addAttribute("order", orderService.getOrderDtoById(orderId));
        model.addAttribute("orderCheck", orderCheckService.createCheckDto(orderId, bankCardDto, user.getId()));
        model.addAttribute("bankCards", bankCardService.getAllUserBankCards(user));

        return "bank/pay_for_order";
    }


    @PostMapping(value = "/pay/{orderId}")
    public String payShipment(@PathVariable Long orderId, @ModelAttribute("checkDto") OrderCheckDto orderCheckDto,
                              @AuthenticationPrincipal User user)
            throws OrderNotFoundException, BankException, CanNotPayException {

        orderCheckDto.setUser(userService.findUserDTOById(user.getId()));

        bankCardService.payForOrder(orderCheckDto);

        log.info("order paying");

        return "redirect:/shipments/show/1/all";
    }

    @GetMapping("/show_check/{id}")
    public String showCheck(@PathVariable Long id, Model model) throws OrderCheckException {

        model.addAttribute("check", Collections.singletonList(orderCheckService.showCheckById(id)));

        return "bank/check_show";
    }

    @GetMapping("/show_checks")
    public String showAllUserCheck(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("check", orderCheckService.showChecksByUser(user.getId()));
        return "bank/check_show";
    }

    @ExceptionHandler(OrderCheckException.class)
    public String handleOrderCheckException(Model model) {
        log.error("OrderCheckException Exception");
        model.addAttribute("error", true);
        return "redirect:/bank/show_checks";
    }

    @ExceptionHandler(BankException.class)
    public String handleBankException(Model model) {
        log.error("BankException Exception");
        model.addAttribute("error", true);
        return "redirect:/bank";
    }

    @ExceptionHandler(CanNotPayException.class)
    public String handleCanNotPayException(Model model) {
        log.error("CanNotPayException Exception");
        model.addAttribute("canNotPay", true);
        return "redirect:/bank";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException(Model model) {
        log.error("OrderNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/bank";
    }
}
