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
class DtoToUserConverterTest {

    @Mock
    DtoToOrderConverter dtoToOrderConverter;

    @Mock
    DtoToCheckConverter dtoToCheckConverter;

    @Mock
    DtoToBankCardConverter dtoToBankCardConverter;

    @InjectMocks
    DtoToUserConverter converter;

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
        Order order = Order.builder().id(ORDER_ID).build();
        OrderCheck orderCheck = OrderCheck.builder().id(ORDER_CHECK_ID).build();
        BankCard bankCard = BankCard.builder().id(BANK_CARD_ID).build();

        UserDto userDto = new UserDto();
        userDto.setId(USER_ID);
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setFirstNameCyr(FIRST_NAME_CYR);
        userDto.setLastNameCyr(LAST_NAME_CYR);
        userDto.setLogin(LOGIN);
        userDto.setEmail(EMAIL);
        userDto.setRole(RoleType.ROLE_USER);

        userDto.getOrders().add(OrderDto.builder().id(ORDER_ID).build());
        userDto.getChecks().add(OrderCheckDto.builder().id(ORDER_CHECK_ID).build());
        userDto.getCards().add(BankCardDto.builder().id(BANK_CARD_ID).build());

        when(dtoToOrderConverter.convert(any(OrderDto.class))).thenReturn(order);
        when(dtoToCheckConverter.convert(any(OrderCheckDto.class))).thenReturn(orderCheck);
        when(dtoToBankCardConverter.convert(any(BankCardDto.class))).thenReturn(bankCard);

        User user = converter.convert(userDto);

        assert user != null;
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstNameCyr(), userDto.getFirstNameCyr());
        assertEquals(user.getLastNameCyr(), userDto.getLastNameCyr());
        assertEquals(user.getLogin(), userDto.getLogin());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getRole(), userDto.getRole());

        verify(dtoToOrderConverter, times(1)).convert(any(OrderDto.class));
        verify(dtoToCheckConverter, times(1)).convert(any(OrderCheckDto.class));
        verify(dtoToBankCardConverter, times(1)).convert(any(BankCardDto.class));
    }
}