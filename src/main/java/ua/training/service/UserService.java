package ua.training.service;

import ua.training.controller.exception.RegException;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

public interface UserService {

    UserDto saveNewUserDto(UserDto userDto) throws RegException;

    UserDto findUserDTOById(Long id);

    User findUserById(Long id);

}
