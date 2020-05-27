package ua.training.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.controller.utility.ProjectPasswordEncoder;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DtoToUserConverterTest {

    @Mock
    ProjectPasswordEncoder passwordEncoder;

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

    final String PASSWORD = "password";

    @Test
    void convert() {

        UserDto userDto = new UserDto();
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setFirstNameCyr(FIRST_NAME_CYR);
        userDto.setLastNameCyr(LAST_NAME_CYR);
        userDto.setLogin(LOGIN);
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);
        userDto.setRole(RoleType.ROLE_USER);

        when(passwordEncoder.encode(any())).thenReturn(PASSWORD);


        User user = converter.convert(userDto);

        assert user != null;
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstNameCyr(), userDto.getFirstNameCyr());
        assertEquals(user.getLastNameCyr(), userDto.getLastNameCyr());
        assertEquals(user.getLogin(), userDto.getLogin());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getRole(), userDto.getRole());

        verify(passwordEncoder, times(1)).encode(anyString());
    }
}