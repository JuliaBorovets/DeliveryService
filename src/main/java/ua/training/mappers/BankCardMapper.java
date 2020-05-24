package ua.training.mappers;

import org.mapstruct.Mapper;

import ua.training.dto.BankCardDTO;
import ua.training.entity.user.BankCard;

@Mapper
public interface BankCardMapper {

    BankCard bankDtoToBank(BankCardDTO bankCardDTO);

    BankCardDTO bankToBankDto(BankCard bankCard);
}
