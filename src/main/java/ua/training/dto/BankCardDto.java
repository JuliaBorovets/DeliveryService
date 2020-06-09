package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class BankCardDto {

    @EqualsAndHashCode.Include
    private Long id;

    private Long expMonth;

    private Long expYear;

    private Long ccv;

    private BigDecimal balance = BigDecimal.ZERO;

}
