package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.mappers.UserMapper;
import ua.training.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void loadUserByUsername() {
        //todo
    }

    @Test
    void findUserById() {
        //todo
    }

    @Test
    void saveNewUserDto() {
        //todo
    }

    @Test
    void findUserDTOById() {
        //todo
    }

    @Test
    void findAllByLoginLike() {
        //todo
    }

    @Test
    void findAllUserDto() {
        //todo
    }

    @Test
    void changeRole() {
        //todo
    }
}