package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.controller.exception.RegException;
import ua.training.controller.utility.ProjectPasswordEncoder;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;
import ua.training.service.UserService;
import javax.annotation.PostConstruct;
import java.util.Locale;
//
@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final ConverterServiceImpl converterService;
    private final ProjectPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ConverterServiceImpl converterService,
                           ProjectPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.converterService = converterService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public void saveNewUser(UserDto user) throws RegException {
        try {
            userRepository.save(createUser(user));
        } catch (DataIntegrityViolationException e) {
            throw new RegException("saveNewUser exception");
        }
    }

    private User createUser(UserDto userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .firstNameCyr(userDTO.getFirstNameCyr())
                .lastName(userDTO.getLastName())
                .lastNameCyr(userDTO.getLastNameCyr())
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
               // .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(RoleType.ROLE_USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
               // .balance(BigDecimal.ZERO)
                .build();
    }

    @PostConstruct
    public void createAdmin() throws RegException {
        if (userRepository.findByLogin("admin").isPresent()) {
            return;
        }

        User admin = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .login("admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("password"))
                //.balance(BigDecimal.ZERO)
                .role(RoleType.ROLE_ADMIN)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        try {
            userRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new RegException("can nit save admin");
        }
    }


//    public UserDTO findUserDTOById(Long id) {
//        return userRepository
//                .findUserById(id)
//              //  .map(UserDTO::new)
//                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
//    }

    public User findUserById(Long id) {
        return userRepository
                .findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
    }


//    public BigDecimal listBankAccountInfo(Long id) {
//
//        UserDTO user = findUserDTOById(id);
//
//        return isLocaleUa() ? user.getBalance() : converterService.convertPriceToLocale(user.getBalance(), localeName());
//    }

    public Long getAdminAccount() {
        User admin = userRepository.findByLogin("admin")
                .orElseThrow(() -> new UsernameNotFoundException("no admin"));

        return admin.getId();
    }


    private boolean isLocaleUa() {
        return LocaleContextHolder.getLocale().equals(new Locale("uk"));
    }

    private String localeName() {
        return LocaleContextHolder.getLocale().toString();
    }

}
