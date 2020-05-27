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
import ua.training.dto.UserDto;
import ua.training.service.serviceImpl.UserServiceImpl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    Model model;

    @InjectMocks
    PageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void mainPage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }

    @Test
    void registerUser() throws Exception {

        mockMvc.perform(get("/reg"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("newUser"));

    }

    @Test
    void newUser() throws Exception {

        mockMvc.perform(post("/reg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("newUser"));

        verify(userService).saveNewUserDto(any(UserDto.class));
    }
}