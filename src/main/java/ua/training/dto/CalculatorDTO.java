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

    @NotNull
    private String calcDestination;

    @NotNull
    private String calcType;

}