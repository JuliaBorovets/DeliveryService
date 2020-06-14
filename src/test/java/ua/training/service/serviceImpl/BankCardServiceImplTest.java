package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.repository.BankCardRepository;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.OrderService;
import ua.training.service.UserService;

@ExtendWith(MockitoExtension.class)
class BankCardServiceImplTest {

    @Mock
    BankCardRepository bankCardRepository;

    @Mock
    UserService userService;

    @Mock
    OrderService orderService;

    @Mock
    OrderCheckRepository orderCheckRepository;

    @InjectMocks
    BankCardServiceImpl service;

    @Test
    void deleteBankCardConnectionWithUser() {
        //todo
    }

    @Test
    void getAllUserBankCards() {
        //todo
    }

    @Test
    void saveBankCardDTO() {
        //todo
    }

    @Test
    void createAccountToSendMoney() {
        //todo
    }

    @Test
    void updateBankCardDTO() {
        //todo
    }

    @Test
    void payForOrder() {
        //todo
    }

    @Test
    void processPaying() {
        //todo
    }

    @Test
    void sendMoney() {
        //todo
    }


    @Test
    void replenishBankCard() {
        //todo
    }

    @Test
    void findBankCardDtoById() {
        //todo
    }

    @Test
    void findBankCardById() {
        //todo
    }
}