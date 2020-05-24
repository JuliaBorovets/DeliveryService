package ua.training.mappers.bankCardMapperImpl;

import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import ua.training.dto.BankCardDTO;
import ua.training.entity.user.BankCard;
import ua.training.mappers.BankCardMapper;
import ua.training.mappers.OrderCheckMapper;
import ua.training.mappers.UserMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



class BankCardMapperImplTest {



    BankCardMapper bankCardMapper = Mappers.getMapper(BankCardMapper.class);


    @BeforeEach
    void setUp() {

    }

    @Test
    void bankDtoToBank() {
        BankCardDTO bankCardDTO = BankCardDTO.builder().id(1L).balance(BigDecimal.ONE).build();
        BankCard bankCard = bankCardMapper.bankDtoToBank(bankCardDTO);

        assertEquals(bankCardDTO.getId(), bankCard.getId());

    }

    @Test
    void bankToBankDto() {
    }
}