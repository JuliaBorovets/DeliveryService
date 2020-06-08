package ua.training.dto;

import lombok.*;
import ua.training.entity.user.RoleType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String login;

    private String email;

    private RoleType role = RoleType.ROLE_USER;

    private String password;

    private List<OrderDto> orders = new ArrayList<>();

    private List<OrderCheckDto> checks = new ArrayList<>();

    private Set<BankCardDto> cards = new HashSet<>();

}