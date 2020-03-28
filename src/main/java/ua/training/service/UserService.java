package ua.training.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.training.controller.exception.RegException;
import ua.training.dto.AddMoneyDTO;
import ua.training.dto.UserDTO;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void saveNewUser(User user) throws RegException {
        try {
            userRepository.save(createUser(user));
        } catch (DataIntegrityViolationException e) {
            RegException regException = new RegException(e);

            if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
                regException.setDuplicate(true);
            } else {
                e.printStackTrace();
            }
            throw regException;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RegException(e);
        }
    }

    private User createUser(User user) {
        return User.builder()
                .firstName(user.getFirstName())
                .firstNameCyr(user.getFirstNameCyr())
                .lastName(user.getLastName())
                .lastNameCyr(user.getLastNameCyr())
                .login(user.getLogin())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(RoleType.ROLE_USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .balance(BigDecimal.ZERO)
                .build();
    }

    public BigDecimal listBankAccountInfo(UserDTO user) {
        return user.getBalance();
    }
}
