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
class CheckToDtoConverterTest {

    @Mock
    UserToUserDtoConverter userToUserDtoConverter;

    @Mock
    OrderToDtoConverter orderToDtoConverter;

    @Mock
    BankCardToDtoConverter bankCardToDtoConverter;

    @InjectMocks
    CheckToDtoConverter converter;

    final Long CHECK_ID = 1L;

    final Long USER_ID = 2L;

    final Long ORDER_ID = 3L;

    final Long BANK_CARD_ID = 4L;

    final BigDecimal PRICE = BigDecimal.valueOf(2);

    final Status STATUS = Status.NOT_PAID;


    @Test
    void convert() {

        UserDto userDto = UserDto.builder().id(USER_ID).build();
        OrderDto orderDto = OrderDto.builder().id(ORDER_ID).build();
        BankCardDto bankCardDto = BankCardDto.builder().id(BANK_CARD_ID).build();

        when(userToUserDtoConverter.convert(any(User.class))).thenReturn(userDto);
        when(orderToDtoConverter.convert(any(Order.class))).thenReturn(orderDto);
        when(bankCardToDtoConverter.convert(any(BankCard.class))).thenReturn(bankCardDto);

        BankCard bankCard = new BankCard();
        bankCard.setId(BANK_CARD_ID);

        User user = new User();
        user.setId(USER_ID);

        Order order = new Order();
        order.setId(ORDER_ID);


        OrderCheck check = new OrderCheck();
        check.setId(CHECK_ID);
        check.setOrder(order);
        check.setPriceInCents(PRICE);
        check.setStatus(STATUS);
        check.setUser(user);
        check.setBankCard(bankCard);

        OrderCheckDto checkDto = converter.convert(check);

        assert checkDto != null;
        assertEquals(check.getId(), checkDto.getId());
        assertEquals(check.getBankCard().getId(), checkDto.getBankCard().getId());
        assertEquals(check.getOrder().getId(), checkDto.getOrder().getId());
        assertEquals(check.getPriceInCents(), checkDto.getPriceInCents());
        assertEquals(check.getStatus(), checkDto.getStatus());
        assertEquals(check.getUser().getId(), checkDto.getUser().getId());

        verify(userToUserDtoConverter, times(1)).convert(any(User.class));
        verify(orderToDtoConverter, times(1)).convert(any(Order.class));
        verify(bankCardToDtoConverter, times(1)).convert(any(BankCard.class));

    }
}