package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

import java.math.BigDecimal;

@Component
public class DtoToBankCardConverter implements Converter<BankCardDto, BankCard> {


    @Synchronized
    @Nullable
    @Override
    public BankCard convert(BankCardDto bankCardDto) {

        final BankCard bankCard = new BankCard();
        bankCard.setId(bankCardDto.getId());
        bankCard.setBalance(BigDecimal.ZERO);

        return bankCard;
    }
}
