package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.BankCardService;
import ua.training.service.OrderCheckService;
import ua.training.service.UserService;
import ua.training.service.serviceImpl.OrderServiceImpl;

import java.util.Collections;

@Slf4j
@RequestMapping("/bank")
@Controller
public class BankController {

    private final OrderServiceImpl orderService;
    private final BankCardService bankCardService;
    private final OrderCheckService orderCheckService;
    private final UserService userService;

    public BankController(OrderServiceImpl orderService, BankCardService bankCardService, OrderCheckService orderCheckService, UserService userService) {
        this.orderService = orderService;
        this.bankCardService = bankCardService;
        this.orderCheckService = orderCheckService;
        this.userService = userService;
    }

    @ModelAttribute
    public User loadModelAttribute(@AuthenticationPrincipal User user,  Model model){

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole().equals(RoleType.ROLE_ADMIN));

        return user;
    }

    @GetMapping
    public String getBankInfo(@AuthenticationPrincipal User user,  Model model){

        model.addAttribute("bankCards", bankCardService.getAllUserBankCards(user));

        return "bank/info";
    }


    @GetMapping("/add_card")
    public String getBankPage(@ModelAttribute("bankDTO") BankCardDto bankCardDTO, Model model) {

        model.addAttribute("bankDTO", bankCardDTO == null ? new BankCardDto() : bankCardDTO);

        return "bank/bank_card_add";
    }


    @PostMapping("/add_card")
    public String addBankCard(@ModelAttribute BankCardDto bankCardDTO, @AuthenticationPrincipal User user, Model model)
            throws BankException {

        bankCardService.saveBankCardDTO(bankCardDTO, user.getId());

        log.error("adding card");

        return "redirect:/bank";
    }

    @GetMapping("/update_card/{cardId}")
    public String getUpdateBankPage(@PathVariable Long cardId, Model model) {

        model.addAttribute("bankDTO", bankCardService.findBankCardDtoById(cardId));

        return "bank/bank_card_update";
    }

    @PostMapping("/update_card/{cardId}")
    public String updateBankCard(@PathVariable Long cardId, @ModelAttribute BankCardDto bankCardDTO,
                                 @AuthenticationPrincipal User user){

        bankCardService.updateBankCardDTO(bankCardDTO);

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
                                            @ModelAttribute("checkDto") OrderCheckDto orderCheckDto, @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws OrderNotFoundException {

        model.addAttribute("order", orderService.getOrderDtoById(orderId));
        model.addAttribute("orderCheck", orderCheckService.createCheckDto(orderId, bankCardDto, user.getId()));
        model.addAttribute("bankCards", bankCardService.getAllUserBankCards(user));

        return "bank/pay_for_order";
    }


    @PostMapping(value = "/pay/{orderId}")
    public String payShipment(@PathVariable Long orderId, @ModelAttribute("checkDto") OrderCheckDto orderCheckDto,
                              @AuthenticationPrincipal User user)
            throws OrderNotFoundException, BankTransactionException, BankException {

        orderCheckDto.setUser(userService.findUserDTOById(user.getId()));

        bankCardService.payForOrder(orderCheckDto);

        log.info("order paying");

        return "redirect:/shipments/show/1/all";
    }

    @GetMapping("/show_check/{id}")
    public String showCheck(@PathVariable Long id, Model model) {

        model.addAttribute("check", Collections.singletonList(orderCheckService.showCheckById(id)));
        return "bank/check_show";
    }

    @GetMapping("/show_checks")
    public String showAllUserCheck(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("check", orderCheckService.showChecksByUser(user.getId()));
        return "bank/check_show";
    }
}
