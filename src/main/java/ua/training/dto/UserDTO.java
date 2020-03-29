package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;
import ua.training.service.ShipmentsTariffs;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    BigDecimal balanceEN;


    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.firstNameCyr = user.getFirstNameCyr();
        this.lastName = user.getLastName();
        this.lastNameCyr = user.getLastNameCyr();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roleType = user.getRole();
        this.balance = user.getBalance();
        this.balanceEN = balance.divide(ShipmentsTariffs.DOLLAR, 2, RoundingMode.HALF_UP);
    }
}