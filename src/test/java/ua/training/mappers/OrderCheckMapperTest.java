package ua.training.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class OrderCheckMapperTest {

    final Long ID = 2L;

    final Long ORDER_ID = 3L;

    final BigDecimal PRICE_IN_CENTS = BigDecimal.valueOf(67);

    final Long BANK_CARD = 7L;

    @Test
    void orderCheckToOrderCheckDto() {

        OrderCheck orderCheck = new  OrderCheck();
        orderCheck.setId(ID);
        orderCheck.setOrder(Order.builder().id(ORDER_ID).build());
        orderCheck.setPriceInCents(PRICE_IN_CENTS);
        orderCheck.setBankCard(BankCard.builder().id(BANK_CARD).build());
        orderCheck.setCreationDate(LocalDate.now());
        orderCheck.setUser(User.builder().id(7L).build());

        OrderCheckDto orderCheckDto = OrderCheckMapper.INSTANCE.orderCheckToOrderCheckDto(orderCheck);
        assertEquals(orderCheckDto.getId(), orderCheck.getId());
        assertEquals(orderCheckDto.getOrderId(), orderCheck.getOrder().getId());
        assertEquals(orderCheckDto.getPriceInCents(), orderCheck.getPriceInCents());
        assertEquals(orderCheckDto.getBankCard(), orderCheck.getBankCard().getId());
        assertEquals(orderCheckDto.getCreationDate(), orderCheck.getCreationDate());
        assertEquals(orderCheckDto.getUserId(), orderCheck.getUser().getId());
    }
}