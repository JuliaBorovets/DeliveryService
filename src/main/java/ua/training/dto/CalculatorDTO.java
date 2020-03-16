package ua.training.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalculatorDTO {

    @NonNull
    private BigDecimal calcWeight;

    @NotBlank
    private String calcDestination;

    @NotBlank
    private String calcType;
}