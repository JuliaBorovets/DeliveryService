package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

@Component
public class DtoToBankCardConverter implements Converter<BankCardDto, BankCard> {
    @Override
    public BankCard convert(BankCardDto bankCardDto) {
        return null;
    }
}
