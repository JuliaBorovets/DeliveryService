package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;

import java.util.ArrayList;
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

    private RoleType role;

    private String password;

    private List<OrderDto> orders = new ArrayList<>();

    private List<OrderCheckDto> checks = new ArrayList<>();

    private List<BankCardDto> cards = new ArrayList<>();

}