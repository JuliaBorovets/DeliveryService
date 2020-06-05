package ua.training.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

@ExtendWith(SpringExtension.class)
class UserMapperTest {

    final Long USER_ID = 1L;

    final String FIRST_NAME = "first name";

    final String LAST_NAME = "last name";

    final String FIRST_NAME_CYR = "first name cyr";

    final String LAST_NAME_CYR = "last name cyr";

    final String LOGIN = "login";

    final String EMAIL = "email";

    @Test
    void userDtoToUser() {

        UserDto userDto = new UserDto();
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setFirstNameCyr(FIRST_NAME_CYR);
        userDto.setLastNameCyr(LAST_NAME_CYR);
        userDto.setLogin(LOGIN);
        userDto.setEmail(EMAIL);
        userDto.setRole(RoleType.ROLE_USER);

//        User user = userMapper.userDtoToUser(userDto);
//
//        assert user != null;
//        assertEquals(user.getFirstName(), userDto.getFirstName());
//        assertEquals(user.getLastName(), userDto.getLastName());
//        assertEquals(user.getFirstNameCyr(), userDto.getFirstNameCyr());
//        assertEquals(user.getLastNameCyr(), userDto.getLastNameCyr());
//        assertEquals(user.getLogin(), userDto.getLogin());
//        assertEquals(user.getEmail(), userDto.getEmail());
//        assertEquals(user.getRole(), userDto.getRole());

    }

    @Test
    void userToUserDto() {

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setFirstNameCyr(FIRST_NAME_CYR);
        user.setLastNameCyr(LAST_NAME_CYR);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setRole(RoleType.ROLE_USER);

//        UserDto userDto = userMapper.userToUserDto(user);
//
//        assert userDto != null;
//        assertEquals(user.getId(), userDto.getId());
//        assertEquals(user.getFirstName(), userDto.getFirstName());
//        assertEquals(user.getLastName(), userDto.getLastName());
//        assertEquals(user.getFirstNameCyr(), userDto.getFirstNameCyr());
//        assertEquals(user.getLastNameCyr(), userDto.getLastNameCyr());
//        assertEquals(user.getLogin(), userDto.getLogin());
//        assertEquals(user.getEmail(), userDto.getEmail());
//        assertEquals(user.getRole(), userDto.getRole());
    }
}