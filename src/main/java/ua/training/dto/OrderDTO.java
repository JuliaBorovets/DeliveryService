package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.order.OrderType;
import ua.training.entity.user.User;
import ua.training.service.ShipmentsTariffs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    Long dtoId;

    OrderType dtoOrderType;

    User dtoOwner;

    BigDecimal dtoWeight;

    Destination dtoDestination;

    String shippingDate;

    OrderStatus dtoOrderStatus;

    BigDecimal dtoShippingPrice;

    String deliveryDate;


}

