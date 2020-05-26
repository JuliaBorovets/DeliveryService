package ua.training.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.dto.UserDto;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.order.Status;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DtoToCheckConverterTest {

    @Mock
    DtoToUserConverter dtoToUserConverter;

    @Mock
    DtoToOrderConverter dtoToOrderConverter;

    @Mock
    DtoToBankCardConverter dtoToBankCardConverter;

    @InjectMocks
    DtoToCheckConverter converter;

    final Long CHECK_ID = 1L;

    final Long USER_ID = 2L;

    final Long ORDER_ID = 3L;

    final Long BANK_CARD_ID = 4L;

    final BigDecimal PRICE = BigDecimal.valueOf(2);

    final Status STATUS = Status.NOT_PAID;

    @Test
    void convert() {

        User user = User.builder().id(USER_ID).build();
        Order order = Order.builder().id(ORDER_ID).build();
        BankCard bankCard = BankCard.builder().id(BANK_CARD_ID).build();

        when(dtoToUserConverter.convert(any(UserDto.class))).thenReturn(user);
        when(dtoToOrderConverter.convert(any(OrderDto.class))).thenReturn(order);
        when(dtoToBankCardConverter.convert(any(BankCardDto.class))).thenReturn(bankCard);

        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(BANK_CARD_ID);

        UserDto userDto = new UserDto();
        userDto.setId(USER_ID);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(ORDER_ID);

        OrderCheckDto checkDto = new OrderCheckDto();
        checkDto.setId(CHECK_ID);
        checkDto.setOrder(orderDto);
        checkDto.setPriceInCents(PRICE);
        checkDto.setStatus(STATUS);
        checkDto.setUser(userDto);
        checkDto.setBankCard(bankCardDto);

        OrderCheck check = converter.convert(checkDto);

        assert check != null;
        assertEquals(check.getId(), checkDto.getId());
        assertEquals(check.getBankCard().getId(), checkDto.getBankCard().getId());
        assertEquals(check.getOrder().getId(), checkDto.getOrder().getId());
        assertEquals(check.getPriceInCents(), checkDto.getPriceInCents());
        assertEquals(check.getStatus(), checkDto.getStatus());
        assertEquals(check.getUser().getId(), checkDto.getUser().getId());

        verify(dtoToUserConverter, times(1)).convert(any(UserDto.class));
        verify(dtoToOrderConverter, times(1)).convert(any(OrderDto.class));
        verify(dtoToBankCardConverter, times(1)).convert(any(BankCardDto.class));

    }
}