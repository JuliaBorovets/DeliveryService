package ua.training.mappers;

import org.mapstruct.Mapper;
import ua.training.dto.UserDTO;
import ua.training.entity.user.User;

@Mapper
public interface UserMapper {

    User userDtoToUser(UserDTO userDTO);

    //UserDTO userToUserDto(User user);
}
