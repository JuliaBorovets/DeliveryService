package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Destination;
//import ua.training.entity.order.OrderStatus;
import ua.training.entity.order.OrderType;
import ua.training.entity.user.User;

import java.math.BigDecimal;

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

  //  OrderStatus dtoOrderStatus;

    BigDecimal dtoShippingPrice;

    String deliveryDate;


}

