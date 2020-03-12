package ua.training.dto;

import lombok.*;
import ua.training.entity.user.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UsersDTO {

    private List<User> users;

}
