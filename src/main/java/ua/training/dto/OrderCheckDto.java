package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderCheckDto {

    private Long id;

    private Long orderId;

    private BigDecimal priceInCents;

    private Status status;

    private UserDto user;

    private BankCardDto bankCard;
}
