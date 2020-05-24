package ua.training.service;

import ua.training.controller.exception.BankException;
import ua.training.dto.BankCardDTO;
import ua.training.dto.UserDTO;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BankCardService {


    BankCardDTO saveBankCardDTO(BankCardDTO bankCardDTO, User user) throws BankException;

    void deleteBankCard(Long bankId) throws BankException;

    void replenishBankCard(Long bankId, BigDecimal balance) throws BankException;

    void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException;

    List<BankCardDTO> getAllUserBankCards(UserDTO user);
}

