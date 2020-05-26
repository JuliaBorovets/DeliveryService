package ua.training.service;

import ua.training.controller.exception.BankException;
import ua.training.controller.exception.BankTransactionException;
import ua.training.dto.BankCardDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface BankCardService {

    void payForOrder(Long orderId) throws BankTransactionException;

    BankCardDto saveBankCardDTO(BankCardDto bankCardDTO, User user) throws BankException;

    void deleteBankCard(Long bankId) throws BankException;

    void replenishBankCard(Long bankId, BigDecimal balance) throws BankException;

    void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException;

    List<BankCardDto> getAllUserBankCards(UserDto user);
}

