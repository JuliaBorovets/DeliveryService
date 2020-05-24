package ua.training.service;

import ua.training.controller.exception.RegException;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

public interface UserService {

    void saveNewUser(UserDto user) throws RegException;

    void createAdmin() throws RegException;

   // UserDTO findUserDTOById(Long id);

    User findUserById(Long id);

   // BigDecimal listBankAccountInfo(Long id);

    Long getAdminAccount();
}
