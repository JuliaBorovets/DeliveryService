package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private LocalDate shippingDate;

    private LocalDate deliveryDate;

    private List<ServiceDto> services = new ArrayList<>();

    private OrderCheckDto check;

}

