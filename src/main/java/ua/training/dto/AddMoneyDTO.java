package ua.training.dto;

import lombok.*;
import ua.training.service.ShipmentsTariffs;

import java.math.BigDecimal;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AddMoneyDTO {

    private BigDecimal amount;

    private BigDecimal amountEN;


    public BigDecimal getAmountEN() {
        return amount.multiply(ShipmentsTariffs.DOLLAR);
    }

}
