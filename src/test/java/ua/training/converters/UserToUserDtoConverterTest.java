package ua.training.converters;

import org.junit.jupiter.api.Test;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserToUserDtoConverterTest {

    UserToUserDtoConverter converter;

    final Long USER_ID = 1L;

    final String FIRST_NAME = "first name";

    final String LAST_NAME = "last name";

    final String FIRST_NAME_CYR = "first name cyr";

    final String LAST_NAME_CYR = "last name cyr";

    final String LOGIN = "login";

    final String EMAIL = "email";

    @Test
    void convert() {

        converter = new UserToUserDtoConverter();

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setFirstNameCyr(FIRST_NAME_CYR);
        user.setLastNameCyr(LAST_NAME_CYR);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setRole(RoleType.ROLE_USER);

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
    }
}