package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

@Component
public class BankCardToDtoConverter implements Converter <BankCard, BankCardDto>{
    @Override
    public BankCardDto convert(BankCard bankCard) {
        return null;
    }
}
