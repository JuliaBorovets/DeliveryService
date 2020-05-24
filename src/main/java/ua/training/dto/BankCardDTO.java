package ua.training.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class BankCardDTO {

    private Long id;

    private BigDecimal balance;

    private Set<UserDTO> usersDto;

    private Set<OrderCheckDTO> orderChecksDto;

}
