package ua.training.dto;

import lombok.*;

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
