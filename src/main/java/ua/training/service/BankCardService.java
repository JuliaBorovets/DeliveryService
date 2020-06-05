package ua.training.service;

import ua.training.controller.exception.BankException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface BankCardService {

    void payForOrder(OrderCheckDto orderCheckDto) throws OrderNotFoundException, BankException;

    void saveBankCardDTO(BankCardDto bankCardDTO, Long userId) throws BankException;

    void updateBankCardDTO(BankCardDto bankCardDTO);

    void deleteBankCard(Long bankId) throws BankException;

    void replenishBankCard(Long bankId, BigDecimal balance) throws BankException;

    List<BankCardDto> getAllUserBankCards(User user);

    BankCardDto findBankCardDtoById(Long id);

    BankCard findBankCardById(Long id);
}

