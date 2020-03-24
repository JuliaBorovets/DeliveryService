package ua.training.dto;


import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalculatorDTO {

    private BigDecimal calcWeight;

    private String calcDestination;

    private String calcType;

    private BigDecimal calcAnnPrice;
}