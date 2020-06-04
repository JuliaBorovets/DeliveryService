package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;
import ua.training.service.OrderService;

@Mapper(uses = {UserMapper.class, DestinationMapper.class, OrderCheckMapper.class, OrderService.class},
        componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto orderToOrderDto(Order order);

}
