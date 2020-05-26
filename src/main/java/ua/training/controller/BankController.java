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
import ua.training.entity.user.User;
import ua.training.service.serviceImpl.OrderServiceImpl;

@Slf4j
@RequestMapping("/bank")
@Controller
public class BankController {


    //private final BankCardServiceImpl bankCardService;
    private final OrderServiceImpl orderService;


    public BankController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"/","/add_card","/replenish/{id}", "/delete_card{id}"})
    public String getBankPage(@PathVariable("id") Long id, @ModelAttribute("bankDTO") BankCardDto bankCardDTO, Model model){

        model.addAttribute("bankDTO", bankCardDTO == null ? new BankCardDto() : bankCardDTO);

        return "bank_card";
    }

    @PostMapping("/add_card")
    public String addBankCard(@ModelAttribute BankCardDto bankCardDTO, @AuthenticationPrincipal User user, Model model)
            throws BankException {

      //  BankCard bankCard = bankCardService.addBankCard(bankCardDTO.getId(), user);
       // model.addAttribute("bankCard", bankCard);
        log.error("adding card");

        return "redirect:/bank/";
    }


    @PostMapping("/replenish/{id}")
    public String replenishCard(@PathVariable("id") Long id, @ModelAttribute BankCardDto bankCardDTO) throws BankException {

       // bankCardService.replenishBankCard(id, bankCardDTO.getMoneyToAdd());
        log.error("replenish card with id = " + id);

        return "redirect:/bank/";
    }

    @DeleteMapping("/delete_card{id}")
    public String deleteBankCard(@PathVariable("id") Long id) throws BankException {

        log.error("deleting card");
      //  bankCardService.deleteBankCard(id);

        return "redirect:/bank/";
    }

    @PostMapping(value = "/pay/{id}")
    public String payShipment(@PathVariable("id") Long shipmentId, @ModelAttribute BankCardDto bankCardDTO)
            throws OrderNotFoundException, BankTransactionException {

      //  orderService.payForOrder(shipmentId);

        log.info("order paying");

        return "redirect:/my_shipments/page/1";
    }

}
