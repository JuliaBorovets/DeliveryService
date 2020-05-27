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
import ua.training.converters.DtoToUserConverter;
import ua.training.converters.UserToUserDtoConverter;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;
import ua.training.repository.UserRepository;
import ua.training.service.UserService;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final DtoToUserConverter dtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserServiceImpl(UserRepository userRepository,DtoToUserConverter dtoToUserConverter,
                           UserToUserDtoConverter userToUserDtoConverter) {
        this.userRepository = userRepository;
        this.dtoToUserConverter = dtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
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

        User user = dtoToUserConverter.convert(userDto);

        try {
            userRepository.save(Objects.requireNonNull(user));
        } catch (DataIntegrityViolationException e) {
            throw new RegException("saveNewUser exception");
        }
        return userToUserDtoConverter.convert(user);
    }


    @Override
    public UserDto findUserDTOById(Long id) {

        return userToUserDtoConverter.convert(findUserById(id));
    }
}
