package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    private Long id;

    private OrderTypeDto orderType;

    private UserDto owner;

    private BigDecimal weight;

    private DestinationDto destination;

    private Status status;

    private String shippingDate;

    private String deliveryDate;

    private BigDecimal shippingPriceInCents;

    private List<ServiceDto> services = new ArrayList<>();

    private OrderCheckDto check;

}

