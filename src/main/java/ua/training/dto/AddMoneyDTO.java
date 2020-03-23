package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class AddMoneyDTO {

    private BigDecimal amount;
}
