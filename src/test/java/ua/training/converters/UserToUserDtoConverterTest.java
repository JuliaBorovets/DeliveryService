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
import ua.training.entity.user.BankCard;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserToUserDtoConverterTest {

    @Mock
    OrderToDtoConverter orderToDtoConverter;

    @Mock
    CheckToDtoConverter checkToDtoConverter;

    @Mock
    BankCardToDtoConverter bankCardToDtoConverter;

    @InjectMocks
    UserToUserDtoConverter converter;

    final Long USER_ID = 1L;

    final Long ORDER_ID = 2L;

    final Long ORDER_CHECK_ID = 3L;

    final Long BANK_CARD_ID = 4L;

    final String FIRST_NAME = "first name";

    final String LAST_NAME = "last name";

    final String FIRST_NAME_CYR = "first name cyr";

    final String LAST_NAME_CYR = "last name cyr";

    final String LOGIN = "login";

    final String EMAIL = "email";

    @Test
    void convert() {

        OrderDto orderDto = OrderDto.builder().id(ORDER_ID).build();
        OrderCheckDto orderCheckDto = OrderCheckDto.builder().id(ORDER_CHECK_ID).build();
        BankCardDto bankCardDto = BankCardDto.builder().id(BANK_CARD_ID).build();

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setFirstNameCyr(FIRST_NAME_CYR);
        user.setLastNameCyr(LAST_NAME_CYR);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setRole(RoleType.ROLE_USER);

        user.getOrders().add(Order.builder().id(ORDER_ID).build());
        user.getChecks().add(OrderCheck.builder().id(ORDER_CHECK_ID).build());
        user.getCards().add(BankCard.builder().id(BANK_CARD_ID).build());

        when(orderToDtoConverter.convert(any(Order.class))).thenReturn(orderDto);
        when(checkToDtoConverter.convert(any(OrderCheck.class))).thenReturn(orderCheckDto);
        when(bankCardToDtoConverter.convert(any(BankCard.class))).thenReturn(bankCardDto);

        UserDto userDto = converter.convert(user);

        assert userDto != null;
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstNameCyr(), userDto.getFirstNameCyr());
        assertEquals(user.getLastNameCyr(), userDto.getLastNameCyr());
        assertEquals(user.getLogin(), userDto.getLogin());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getRole(), userDto.getRole());

        verify(orderToDtoConverter, times(1)).convert(any(Order.class));
        verify(checkToDtoConverter, times(1)).convert(any(OrderCheck.class));
        verify(bankCardToDtoConverter, times(1)).convert(any(BankCard.class));

    }
}