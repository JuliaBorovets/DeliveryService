package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;
import ua.training.entity.order.OrderType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    Long id;

    @Size(min = 2, max = 90)
    String dtoDescription;

    @NotNull
    OrderType dtoOrderType;

    @NotNull
    BigDecimal dtoWeight;

    @NotNull
    Destination dtoDestination;

    LocalDate shippingDate;
    LocalDate dtoDeliveryDate;

    BigDecimal shippingPrice;

    OrderStatus orderStatus;

    String deliveryDate;
    String status;
    String type;
    String destination;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.dtoDescription = order.getDescription();
        this.dtoOrderType = order.getOrderType();
        this.dtoWeight = order.getWeight();
        this.dtoDestination = order.getDestination();
        this.shippingDate = order.getShippingDate();
        this.dtoDeliveryDate = order.getDeliveryDate();
        this.shippingPrice = order.getShippingPrice();
        this.orderStatus = order.getOrderStatus();
    }
}
