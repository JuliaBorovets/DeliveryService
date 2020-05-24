package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderCheckDto {

    private Long id;

    private OrderDto order;

    private BigDecimal priceInCents;

    private Status status;

    private UserDto user;

    private BankCardDto bankCard;

}
