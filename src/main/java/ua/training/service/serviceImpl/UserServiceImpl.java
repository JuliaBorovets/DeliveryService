package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.RegException;
import ua.training.controller.utility.ProjectPasswordEncoder;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.mappers.UserMapper;
import ua.training.repository.UserRepository;
import ua.training.service.UserService;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final ProjectPasswordEncoder encoder;


    public UserServiceImpl(UserRepository userRepository, ProjectPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }


    public User findUserById(Long id) {
        return userRepository
                .findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = RegException.class)
    @Override
    public UserDto saveNewUserDto(UserDto userDto) throws RegException {

        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        ProjectPasswordEncoder encoder = new ProjectPasswordEncoder();

        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(RoleType.ROLE_USER);

        try {
            userRepository.save(Objects.requireNonNull(user));
        } catch (DataIntegrityViolationException e) {
            throw new RegException("saveNewUser exception");
        }
        return UserMapper.INSTANCE.userToUserDto(user);
    }


    @Override
    public UserDto findUserDTOById(Long id) {

        return UserMapper.INSTANCE.userToUserDto(findUserById(id));
    }
}
