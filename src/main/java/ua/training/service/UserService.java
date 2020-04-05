package ua.training.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.training.controller.exception.RegException;
import ua.training.dto.UserDTO;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${constants.BASE.PRICE}")
    Integer BASE_PRICE;

    @Value("${constant.DOLLAR}")
    BigDecimal DOLLAR;

    @Value("${constants.COEFFICIENT}")
    Double COEFFICIENT;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void saveNewUser(UserDTO user) throws RegException {
        try {
            userRepository.save(createUser(user));
        } catch (DataIntegrityViolationException e) {
            throw new RegException("saveNewUser exception");
        }
    }

    private User createUser(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .firstNameCyr(userDTO.getFirstNameCyr())
                .lastName(userDTO.getLastName())
                .lastNameCyr(userDTO.getLastNameCyr())
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(RoleType.ROLE_USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .balance(BigDecimal.ZERO)
                .build();
    }

    @PostConstruct
    private void createAdmin() throws RegException {
        if (userRepository.findByLogin("admin").isPresent()) {
            return;
        }

        User admin = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .login("admin")
                .email("admin@gmail.com")
                .password("password")
                .balance(BigDecimal.ZERO)
                .role(RoleType.ROLE_ADMIN)
                .build();

        try {
            userRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new RegException("can nit save admin");
        }
    }


    public UserDTO findUserDTOById(Long id) {
        return userRepository
                .findUserById(id)
                .map(UserDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
    }

    public User findUserById(Long id) {
        return userRepository
                .findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
    }


    public BigDecimal listBankAccountInfo(Long id) {

        UserDTO user = findUserDTOById(id);

        return isLocaleUa() ? user.getBalance() : convertBalanceToLocale(user.getBalance());
    }

    private BigDecimal convertBalanceToLocale(BigDecimal balance) {
        log.error("converting to locale");
        return balance.divide(DOLLAR, 2, RoundingMode.HALF_UP);
    }

    private boolean isLocaleUa() {
        return LocaleContextHolder.getLocale().equals(new Locale("uk"));
    }

}
