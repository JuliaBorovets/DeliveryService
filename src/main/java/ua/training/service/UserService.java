package ua.training.service;

import lombok.extern.slf4j.Slf4j;
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

    public UserDTO findUserDTOById(Long id) {
        return new UserDTO(userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found")));

    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));

    }


    public BigDecimal listBankAccountInfo(Long id, boolean isLocaleEn) {

        UserDTO user = findUserDTOById(id);
        if (isLocaleEn) {
            user.setBalance(user.getBalanceEN());
        } else user.setBalance(user.getBalance());

        return user.getBalance();
    }


}
