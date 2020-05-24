package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static ua.training.dto.RegexConstants.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {

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

    private List<OrderDTO> orders;

    private List<OrderCheckDTO> checks;

    private List<BankCardDTO> cards;

}