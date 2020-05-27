package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTypeToDtoConverterTest {

    final Long ID = 1L;
    final String NAME = "type";
    final BigDecimal PRICE = BigDecimal.valueOf(50);

    OrderTypeToDtoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new OrderTypeToDtoConverter();
    }

    @Test
    void convert() {
        OrderType  orderType = OrderType.builder().id(ID).name(NAME).priceInCents(PRICE).build();

        OrderTypeDto orderTypeDto = converter.convert(orderType);


        assertEquals(orderType.getId(), orderTypeDto.getId());
        assertEquals(orderType.getName(), orderTypeDto.getName());
        assertEquals(orderType.getPriceInCents(), orderTypeDto.getPriceInCents());
    }
}