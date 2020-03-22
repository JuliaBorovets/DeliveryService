package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankAccountDTO {

    private BigDecimal balance;

}
