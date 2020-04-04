package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AddMoneyDTO {

    private BigDecimal amount;

}
