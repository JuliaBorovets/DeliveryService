package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    @Mappings({
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "checks", ignore = true),
            @Mapping(target = "cards", ignore = true)
    })
    User userDtoToUser(UserDto userDto);
}
