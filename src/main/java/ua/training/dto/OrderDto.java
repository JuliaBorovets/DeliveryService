package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    private Long id;

    private String description;

    private OrderTypeDto orderType;

    private DestinationDto destination;

    private BigDecimal weight;

    private Status status;

    private String shippingDate;

    private String deliveryDate;

    private BigDecimal shippingPriceInCents;

    private OrderCheckDto check;

    private String destinationCityFrom;

    private String destinationCityTo;

    private String type;

}

