package ua.training.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class BankCardDto {

    private Long id;

    private BigDecimal balance;

    private List<UserDto> users;

    private List<OrderCheckDto> orderChecks;

}
