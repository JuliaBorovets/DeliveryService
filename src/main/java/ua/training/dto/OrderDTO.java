package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.order.OrderType;
import ua.training.entity.user.User;

import java.math.BigDecimal;
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

    LocalDate dtoShippingDate;

    OrderStatus dtoOrderStatus;

    BigDecimal dtoShippingPrice;

    LocalDate dtoDeliveryDate;


    String shippingDate;
    String deliveryDate;
    String status;
    String type;
    String destination;

    public OrderDTO(Order order) {
        this.dtoId = order.getId();
        this.dtoOrderType = order.getOrderType();
        this.dtoOwner = order.getOwner();
        this.dtoWeight = order.getWeight();
        this.dtoDestination = order.getDestination();
        this.dtoShippingDate = order.getShippingDate();
        this.dtoOrderStatus = order.getOrderStatus();
        this.dtoShippingPrice = order.getShippingPrice();
        this.dtoDeliveryDate = order.getDeliveryDate();
    }

}

