package ua.training.service;

import ua.training.controller.exception.RegException;
import ua.training.dto.UserDto;

public interface UserService {

    UserDto saveNewUserDto(UserDto userDto) throws RegException;

    UserDto findUserDTOById(Long id);

}
