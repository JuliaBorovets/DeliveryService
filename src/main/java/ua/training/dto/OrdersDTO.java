package ua.training.dto;

import lombok.*;
import ua.training.entity.order.Order;
import ua.training.entity.user.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrdersDTO {

    private List<Order> orders;

}
