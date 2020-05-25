package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

@Component
public class BankCardToDtoConverter implements Converter <BankCard, BankCardDto>{

    @Synchronized
    @Nullable
    @Override
    public BankCardDto convert(BankCard bankCard) {

        final BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(bankCard.getId());
        bankCardDto.setBalance(bankCard.getBalance());

        return bankCardDto;
    }
}
