package ua.training.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ua.training.controller.utility.ValidPassword;
import ua.training.entity.user.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

    private Long id;

    @Pattern(regexp = RegexConstants.firstNameRegexp)
    private String firstName;

    @Length(min = 3, max = 30)
    private String lastName;

    @Length(min = 8, max = 30)
    private String login;

    @Email
    private String email;

    private RoleType role = RoleType.ROLE_USER;

    @ValidPassword
    private String password;


}