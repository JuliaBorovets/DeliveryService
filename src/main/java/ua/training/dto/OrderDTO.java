package ua.training.dto;

import lombok.*;
import ua.training.entity.user.User;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    @NotBlank(message = "description can not be blank")
    String dtoDescription;

    @NotBlank(message = "type may can not be blank")
    String dtoOrderType;

    @NotBlank(message = "weight can not be blank")
    BigDecimal dtoWeight;

    @NotBlank(message = "announced price can not be blank")
    BigDecimal dtoAnnouncedPrice;


}
