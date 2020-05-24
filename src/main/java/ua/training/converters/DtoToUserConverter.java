package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

@Component
public class DtoToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto userDto) {
        return null;
    }
}
