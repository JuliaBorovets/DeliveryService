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

    @DecimalMin("0.500")
    private BigDecimal calcWeight;

    @NotBlank
    private String calcDestination;

    @NotBlank
    private String calcType;

    @DecimalMin("0.500")
    private BigDecimal calcAnnPrice;
}