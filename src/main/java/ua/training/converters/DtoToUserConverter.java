package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.controller.utility.ProjectPasswordEncoder;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

@Component
public class DtoToUserConverter implements Converter<UserDto, User> {

    private final ProjectPasswordEncoder passwordEncoder;

    public DtoToUserConverter(ProjectPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Synchronized
    @Nullable
    @Override
    public User convert(UserDto userDto) {

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setFirstNameCyr(userDto.getFirstNameCyr());
        user.setLastNameCyr(userDto.getLastNameCyr());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setRole(RoleType.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        return user;
    }
}
