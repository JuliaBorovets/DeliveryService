package ua.training.service.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.controller.exception.BankException;
import ua.training.entity.user.BankCard;
import ua.training.repository.BankCardRepository;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BankCardServiceImplTest {

    @Mock
    BankCardRepository bankCardRepository;

    BankCard bankCard;


    @BeforeEach
    void setUp() {
        bankCard = new BankCard();
    }

    @Test
    void addBankCard() throws BankException {

    }

    @Test
    void deleteBankCard() {

    }

    @Test
    void replenishBankCard() {
    }

    @Test
    void sendMoney() {
    }
}