package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class BankCardToDtoConverterTest {

    BankCardToDtoConverter converter;

    final Long ID = 1L;
    final BigDecimal BALANCE = BigDecimal.valueOf(22);

    @BeforeEach
    void setUp() {
        converter = new BankCardToDtoConverter();
    }

    @Test
    void convert() {

        BankCard bankCard = BankCard.builder().id(ID).balance(BALANCE).build();

        BankCardDto bankCardDto = converter.convert(bankCard);

        assert bankCardDto != null;
        assertEquals(bankCard.getId(), bankCardDto.getId());
        assertEquals(bankCard.getBalance(), bankCardDto.getBalance());

    }
}