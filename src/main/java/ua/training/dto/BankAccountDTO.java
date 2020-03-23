package ua.training.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class BankAccountDTO {

    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;


}
