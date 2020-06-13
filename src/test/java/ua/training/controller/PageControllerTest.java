package ua.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.training.controller.utility.GlobalExceptionHandler;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    PageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void mainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void registerUser() throws Exception {
        mockMvc.perform(get("/reg"))
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("newUser"));
    }

    @Test
    void newUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(post("/reg")
                .flashAttr("newUser", userDto)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(model().attributeExists("newUser"));
        verify(userService).saveNewUserDto(any());
    }


    @Test
    void loginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login_page"))
                .andExpect(status().isOk());
    }

    @Test
    void accountPage() throws Exception {

        User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .login("loginnnnn")
                .role(RoleType.ROLE_ADMIN)
                .password("3848password")
                .email("email@g.dd").build();

        mockMvc.perform(get("/account_page")
                .flashAttr("user", user)
        )
                .andExpect(view().name("user/account_page"))
                .andExpect(model().attribute("isAdmin", true))
                .andExpect(status().isOk());
    }

    @Test
    void localRedirect() throws Exception {
        mockMvc.perform(post("/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account_page"));
    }

}