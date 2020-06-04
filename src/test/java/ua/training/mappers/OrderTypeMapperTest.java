package ua.training.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderTypeMapperImpl.class)
class OrderTypeMapperTest {

    final Long ID = 1L;
    final String NAME = "type";
    final BigDecimal PRICE = BigDecimal.valueOf(50);

    @Autowired
    OrderTypeMapper mapper;

    @Test
    void orderTypeDtoToOrderType() {
        OrderType orderType = OrderType.builder().id(ID).name(NAME).priceInCents(PRICE).build();

        OrderTypeDto orderTypeDto = mapper.orderTypeToOrderTypeDto(orderType);


        assertEquals(orderType.getId(), orderTypeDto.getId());
        assertEquals(orderType.getName(), orderTypeDto.getName());
        assertEquals(orderType.getPriceInCents(), orderTypeDto.getPriceInCents());
    }

    @Test
    void testOrderTypeDtoToOrderType() {
        OrderTypeDto orderTypeDto = OrderTypeDto.builder().id(ID).name(NAME).priceInCents(PRICE).build();

        OrderType orderType = mapper.orderTypeDtoToOrderType(orderTypeDto);

        assertEquals(orderType.getId(), orderTypeDto.getId());
        assertEquals(orderType.getName(), orderTypeDto.getName());
        assertEquals(orderType.getPriceInCents(), orderTypeDto.getPriceInCents());
    }
}