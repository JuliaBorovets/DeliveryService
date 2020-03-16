package ua.training.service;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.training.controller.RegException;
import ua.training.dto.UserDTO;
import ua.training.dto.UsersDTO;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;

import javax.validation.constraints.NotNull;

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

    public UsersDTO getAllUsers() {
        return new UsersDTO(userRepository.findAll());
    }

    public void saveNewUser(User user) throws RegException {
        try {
            userRepository.save(getUserWithPermissions(user));
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

    private User getUserWithPermissions(User user) {
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
                .build();
    }
}
