package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

@Mapper(componentModel = "spring")
public interface BankCardMapper {

    BankCardMapper INSTANCE = Mappers.getMapper(BankCardMapper.class);

    BankCardDto bankCardToDto(BankCard bankCard);

    BankCard bankCardDtoToBankCard(BankCardDto bankCardDto);
}
