package ua.training.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.training.dto.BankCardDto;
import ua.training.entity.user.BankCard;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankCardMapperImpl.class)
class BankCardMapperTest {

    @Autowired
    BankCardMapper mapper;

    final Long ID = 1L;
    final BigDecimal BALANCE = BigDecimal.valueOf(22);


    @Test
    void bankCardToDto() {
        BankCard bankCard = BankCard.builder().id(ID).balance(BALANCE).build();

        BankCardDto bankCardDto = mapper.bankCardToDto(bankCard);

        assert bankCardDto != null;
        assertEquals(bankCard.getId(), bankCardDto.getId());
        assertEquals(bankCard.getBalance(), bankCardDto.getBalance());

    }

    @Test
    void bankCardDtoToBankCard() {
        BankCardDto bankCardDto = BankCardDto.builder().id(ID).balance(BigDecimal.ZERO).build();

        BankCard bankCard = mapper.bankCardDtoToBankCard(bankCardDto);

        assert bankCard != null;
        assertEquals(bankCard.getId(), bankCardDto.getId());
        System.out.println(bankCard.getBalance());
        assertEquals(bankCard.getBalance(), BigDecimal.ZERO);
    }
}