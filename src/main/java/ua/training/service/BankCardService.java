package ua.training.service;

import ua.training.controller.exception.BankException;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;

public interface BankCardService {


    BankCard addBankCard(Long bankId, User user) throws BankException;

    void deleteBankCard(Long bankId) throws BankException;

    void replenishBankCard(Long bankId, BigDecimal balance) throws BankException;

    void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException;
}

