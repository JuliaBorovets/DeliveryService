package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReplenishDto {

    private BigDecimal moneyToAdd;
}
