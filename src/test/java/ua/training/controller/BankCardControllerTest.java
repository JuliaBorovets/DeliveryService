package ua.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import ua.training.service.serviceImpl.OrderServiceImpl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class BankCardControllerTest {


    @Mock
    OrderServiceImpl orderService;

    @InjectMocks
    BankController controller;

    @Mock
    Model model;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getBankPage() throws Exception {

        mockMvc.perform(get("/bank/replenish/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank_card"));

    }

    @Test
    void addBankCard() {
    }

    @Test
    void replenishCard() {
    }

    @Test
    void deleteBankCard() {
    }

    @Test
    void payShipment() {
    }
}