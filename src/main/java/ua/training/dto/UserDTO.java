package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    Long id;


    @Pattern(regexp = "[A-Z][a-z]{2,20}")
    String firstName;

    @Pattern(regexp = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{2,20}")
    String firstNameCyr;


    @Pattern(regexp = "[a-zA-Z]+'?-?[a-zA-Z]+\\s?([a-zA-Z]+)?")
    String lastName;


    @Pattern(regexp = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25}([-][А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25})?")
    String lastNameCyr;

    @Pattern(regexp = "[a-z0-9_-]{3,20}")
    String login;

    @Email
    String email;

    @NotNull
    String password;


    RoleType roleType;

    BigDecimal balance;

}