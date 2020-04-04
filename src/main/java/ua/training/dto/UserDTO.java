package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static ua.training.dto.RegexConstants.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    Long id;

    @Pattern(regexp = firstNameRegexp)
    String firstName;

    @Pattern(regexp = firstNameCyrRegexp)
    String firstNameCyr;

    @Pattern(regexp = lastNameRegexp)
    String lastName;

    @Pattern(regexp = lastNameCyrRegexp)
    String lastNameCyr;

    @Pattern(regexp = loginRegexp)
    String login;

    @Email
    String email;

    @NotNull
    String password;

    RoleType roleType;

    BigDecimal balance;


    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.firstNameCyr = user.getFirstNameCyr();
        this.lastName = user.getLastName();
        this.lastNameCyr = user.getLastNameCyr();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.roleType = user.getRole();
        this.balance = user.getBalance();
    }
}