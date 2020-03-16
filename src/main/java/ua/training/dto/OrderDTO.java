package ua.training.dto;

import lombok.*;
import ua.training.entity.user.User;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    @Size(min = 2, max = 90)
    String dtoDescription;

    @NotNull
    String dtoOrderType;

    @NotNull
    BigDecimal dtoWeight;

    @NotNull
    BigDecimal dtoAnnouncedPrice;

    @NotNull
    String dtoDestination;

}
