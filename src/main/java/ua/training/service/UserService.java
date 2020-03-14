package ua.training.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.dto.UserDTO;
import ua.training.dto.UsersDTO;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;

import javax.validation.constraints.NotNull;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        return new UserDTO(userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("login " + username + " not found.")));
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UsersDTO getAllUsers() {
        return new UsersDTO(userRepository.findAll());
    }

    public void saveNewUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        }
    }
}
