package ua.training.mappers;

import org.mapstruct.Mapper;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Order;

@Mapper
public interface OrderMapper {

    Order orderDtoToOrder(OrderDTO orderDTO);

    OrderDTO orderToOrderDto(Order order);
}
