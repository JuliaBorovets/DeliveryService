package ua.training.service.serviceImpl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    DtoToUserConverter dtoToUserConverter;
//
//    @Mock
//    UserToUserDtoConverter userToUserDtoConverter;
//
//    @InjectMocks
//    UserServiceImpl service;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void loadUserByUsername() {
//        final String USERNAME = "username";
//        User user = new User();
//        user.setLogin(USERNAME);
//
//
//        when(userRepository.findByLogin(USERNAME)).thenReturn(Optional.of(user));
//
//        User foundUser = (User) service.loadUserByUsername(USERNAME);
//        assertEquals(foundUser.getId(), user.getId());
//
//        verify(userRepository, times(1)).findByLogin(anyString());
//    }
//
//    @Test
//    void findUserById() {
//        final Long ID = 1L;
//
//        User user = new User();
//        user.setId(ID);
//
//        Optional<User> optionalUser = Optional.of(user);
//
//        when(userRepository.findUserById(anyLong())).thenReturn(optionalUser);
//
//        User foundUser = service.findUserById(ID);
//
//        verify(userRepository, times(1)).findUserById(anyLong());
//        verify(userRepository, never()).findAll();
//
//        assertEquals(foundUser.getId(), ID);
//    }
//
//    @Transactional
//    @Test
//    void saveNewUserDto() throws RegException {
//
//        final Long ID = 1L;
//        final String LOGIN = "login";
//
//        User userToSave = new User();
//        userToSave.setId(ID);
//        userToSave.setLogin(LOGIN);
//
//        UserDto userDto = new UserDto();
//        userDto.setId(ID);
//        userDto.setLogin(LOGIN);
//
//        when(userRepository.save(any(User.class))).thenReturn(userToSave);
//        when(dtoToUserConverter.convert(any(UserDto.class))).thenReturn(userToSave);
//        when(userToUserDtoConverter.convert(any(User.class))).thenReturn(userDto);
//
//        UserDto savedUser = service.saveNewUserDto(new UserDto());
//
//        assertEquals(savedUser.getId(), ID);
//        assertEquals(savedUser.getLogin(), LOGIN);
//
//        verify(userRepository).save(any(User.class));
//        verify(dtoToUserConverter).convert(any(UserDto.class));
//        verify(userToUserDtoConverter).convert(any(User.class));
//
//    }
//
//    @Test
//    void findUserDTOById() {
//
//        final Long ID = 1L;
//
//        UserDto userDto = new UserDto();
//        userDto.setId(ID);
//
//        User user = new User();
//        user.setId(ID);
//
//        Optional<User> optionalUser = Optional.of(user);
//
//        when(userRepository.findUserById(anyLong())).thenReturn(optionalUser);
//
//        when(userToUserDtoConverter.convert(any(User.class))).thenReturn(userDto);
//
//        UserDto foundUserDto = service.findUserDTOById(ID);
//
//        assertEquals(foundUserDto.getId(), ID);
//        verify(userRepository, times(1)).findUserById(anyLong());
//        verify(userToUserDtoConverter, times(1)).convert(any());
//
//    }
}