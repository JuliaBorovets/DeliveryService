package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DtoToOrderTypeConverterTest {

    final Long ID = 1L;
    final String NAME = "type";
    final BigDecimal PRICE = BigDecimal.valueOf(50);

    DtoToOrderTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DtoToOrderTypeConverter();
    }

    @Test
    void convert() {
        OrderTypeDto orderTypeDto = OrderTypeDto.builder().id(ID).name(NAME).priceInCents(PRICE).build();

        OrderType orderType = converter.convert(orderTypeDto);

        assertEquals(orderType.getId(), orderTypeDto.getId());
        assertEquals(orderType.getName(), orderTypeDto.getName());
        assertEquals(orderType.getPriceInCents(), orderTypeDto.getPriceInCents());

    }
}