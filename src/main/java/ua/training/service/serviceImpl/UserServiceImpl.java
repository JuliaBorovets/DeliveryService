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
import ua.training.mappers.DtoToUserConverter;
import ua.training.mappers.UserToUserDtoConverter;
import ua.training.repository.UserRepository;
import ua.training.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final DtoToUserConverter dtoToUserConverter;


    public UserServiceImpl(UserRepository userRepository, UserToUserDtoConverter userToUserDtoConverter,
                           DtoToUserConverter dtoToUserConverter) {
        this.userRepository = userRepository;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.dtoToUserConverter = dtoToUserConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }


    @Override
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
        ProjectPasswordEncoder encoder = new ProjectPasswordEncoder();

        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

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


    @Override
    public List<UserDto> findAllUserDto() {
        return userRepository.findAll().stream()
                .map(userToUserDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void changeRole(Long userId) {
        User user = findUserById(userId);

        if (user.getRole().equals(RoleType.ROLE_ADMIN)){
            user.setRole(RoleType.ROLE_USER);
        } else {
            user.setRole(RoleType.ROLE_ADMIN);
        }
        userRepository.save(user);
    }
}
