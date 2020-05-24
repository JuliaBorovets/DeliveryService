package ua.training.service;

import ua.training.controller.exception.RegException;
import ua.training.dto.UserDTO;
import ua.training.entity.user.User;

import java.math.BigDecimal;

public interface UserService {

    void saveNewUser(UserDTO user) throws RegException;

    void createAdmin() throws RegException;

   // UserDTO findUserDTOById(Long id);

    User findUserById(Long id);

   // BigDecimal listBankAccountInfo(Long id);

    Long getAdminAccount();
}
