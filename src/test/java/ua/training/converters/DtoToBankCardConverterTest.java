package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoToBankCardConverterTest {

    DtoToBankCardConverter cardConverter;

    final Long ID = 1L;

    @BeforeEach
    void setUp() {
        cardConverter = new DtoToBankCardConverter();
    }

    @Test
    void convert() {
        BankCardDto bankCardDto = BankCardDto.builder().id(ID).build();

        BankCard bankCard = cardConverter.convert(bankCardDto);

        assert bankCard != null;
        assertEquals(bankCard.getId(), bankCardDto.getId());
        assertEquals(bankCard.getBalance(), BigDecimal.ZERO);
    }
}