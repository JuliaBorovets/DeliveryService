package ua.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.training.controller.exception.BankException;
import ua.training.controller.exception.OrderCheckException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.controller.utility.GlobalExceptionHandler;
import ua.training.dto.BankCardDto;
import ua.training.dto.OrderCheckDto;
import ua.training.dto.OrderDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.BankCardService;
import ua.training.service.OrderCheckService;
import ua.training.service.OrderService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BankControllerTest {

    @Mock
    OrderService orderService;

    @Mock
    BankCardService bankCardService;

    @Mock
    OrderCheckService orderCheckService;


    @InjectMocks
    BankController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getBankInfo() throws Exception {
        List<BankCardDto> bankCardDtoList = Arrays.asList(new BankCardDto(), new BankCardDto());

        when(bankCardService.getAllUserBankCards(any())).thenReturn(bankCardDtoList);

        mockMvc.perform(get("/bank"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank/info"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("canNotPay"))
                .andExpect(model().attribute("bankCards", hasSize(bankCardDtoList.size())));

        verify(bankCardService).getAllUserBankCards(any());
    }

    @Test
    void getBankPage() throws Exception {
        mockMvc.perform(get("/bank/add_card"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bankDTO"))
                .andExpect(view().name("bank/bank_card_add"));
    }

    @Test
    void addBankCard() throws Exception {

        BankCardDto bankCardDto =  BankCardDto.builder().id(3L).expMonth(4L).expYear(2030L).ccv(33L).build();

        mockMvc.perform(post("/bank/add_card")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("bankDTO", bankCardDto)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bank"));

        verify(bankCardService).saveBankCardDTO(any(), any());
    }

    @Test
    void getUpdateBankPage() throws Exception {

        when(bankCardService.findBankCardDtoById(any())).thenReturn(new BankCardDto());

        mockMvc.perform(get("/bank/update_card/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank/bank_card_update"))
                .andExpect(model().attributeExists("bankDTO"));

        verify(bankCardService).findBankCardDtoById(any());
    }

    @Test
    void updateBankCard() throws Exception {
        BankCardDto bankCardDto =  BankCardDto.builder().id(3L).expMonth(4L).expYear(2030L).ccv(33L).build();

        mockMvc.perform(post("/bank/update_card/1")
                .flashAttr("bankDTO", bankCardDto)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bank"));

        verify(bankCardService).updateBankCardDTO(any(), any());

    }

    @Test
    void deleteBankCard() throws Exception {

        mockMvc.perform(get("/bank/delete_card/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bank"));

        verify(bankCardService).deleteBankCardConnectionWithUser(anyLong(), any());
    }

    @Test
    void payParticularShipmentView() throws Exception {

        when(orderService.getOrderDtoById(anyLong())).thenReturn(new OrderDto());
        when(orderCheckService.createCheckDto(anyLong(), any(), any())).thenReturn(new OrderCheckDto());
        when(bankCardService.getAllUserBankCards(any())).thenReturn(Arrays.asList(new BankCardDto(), new BankCardDto()));

        mockMvc.perform(get("/bank/pay/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank/pay_for_order"))
                .andExpect(model().attributeExists("checkDto"))
                .andExpect(model().attributeExists("bankCard"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("orderCheck"))
                .andExpect(model().attributeExists("bankCards"));

        verify(orderService).getOrderDtoById(anyLong());
        verify(orderCheckService).createCheckDto(anyLong(), any(), any());
        verify(bankCardService).getAllUserBankCards(any());
    }

    @Test
    void payShipment() throws Exception {

        mockMvc.perform(post("/bank/pay/1")
                .flashAttr("checkDto", new OrderCheckDto())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/shipments/show/1/all"));

        verify(bankCardService).payForOrder(any());
    }

    @Test
    void showCheck() throws Exception {

        when(orderCheckService.showCheckById(anyLong())).thenReturn(new OrderCheckDto());

        mockMvc.perform(get("/bank/show_check/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank/check_show"))
                .andExpect(model().attributeExists("check"));

        verify(orderCheckService).showCheckById(anyLong());
    }

    @Test
    void showAllUserCheck() throws Exception {

        List<OrderCheckDto> orderCheckDtoList = Arrays.asList(new OrderCheckDto(), new OrderCheckDto());

        when(orderCheckService.showChecksByUser(anyLong())).thenReturn(orderCheckDtoList);

        User user = User.builder().id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/bank/show_checks")
                .flashAttr("user", user)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("bank/check_show"))
                .andExpect(model().attribute("check", hasSize(orderCheckDtoList.size())));

        verify(orderCheckService).showChecksByUser(anyLong());
    }

    @Test
    void handleOrderCheckException() throws Exception {

        when(orderCheckService.showCheckById(anyLong())).thenThrow(new OrderCheckException("OrderCheckException"));

        mockMvc.perform(get("/bank/show_check/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", true))
                .andExpect(view().name("redirect:/bank/show_checks"));

        verify(orderCheckService).showCheckById(anyLong());
    }

    @Test
    void handleBankException() throws Exception {
        when(bankCardService.findBankCardDtoById(any())).thenThrow(new BankException("bank exception"));

        mockMvc.perform(get("/bank/update_card/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", true))
                .andExpect(view().name("redirect:/bank"));

        verify(bankCardService).findBankCardDtoById(any());

    }

    @Test
    void handleOrderNotFoundException() throws Exception {
        when(orderService.getOrderDtoById(anyLong())).thenThrow(new OrderNotFoundException("OrderNotFoundException"));

        mockMvc.perform(get("/bank/pay/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", true))
                .andExpect(view().name("redirect:/bank"));

        verify(orderService).getOrderDtoById(anyLong());

    }
}