package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String firstNameCyr;

    private String lastNameCyr;

    private String login;

    private String email;

    private String password;

    private RoleType role;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private List<OrderDto> orders;

    private List<OrderCheckDto> checks;

    private List<BankCardDto> cards;

}