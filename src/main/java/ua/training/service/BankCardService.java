package ua.training.service;

import ua.training.controller.exception.BankException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.BankCardDto;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface BankCardService {

    Long payForOrder(Long orderId, Long bankCardId, UserDto userDto) throws BankException, OrderNotFoundException;

    BankCardDto saveBankCardDTO(BankCardDto bankCardDTO, Long userId) throws BankException;

    void deleteBankCard(Long bankId) throws BankException;

    void replenishBankCard(Long bankId, BigDecimal balance) throws BankException;

    List<BankCardDto> getAllUserBankCards(User user);

    BankCardDto findBankCardDtoById(Long id);

}

