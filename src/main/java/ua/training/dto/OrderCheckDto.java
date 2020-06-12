package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    private Long userId;

    private Long bankCard;

    private LocalDate creationDate;
}
