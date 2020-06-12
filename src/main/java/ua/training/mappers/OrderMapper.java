package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Mapper(uses = {OrderTypeMapper.class, DestinationMapper.class, OrderCheckMapper.class}, componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(target = "orderType", ignore = true),
            @Mapping(target = "destination", ignore = true),
            @Mapping(target = "check", ignore = true),
            @Mapping(target = "shippingPriceInCents", ignore = true)
    })
    Order orderDtoToOrder(OrderDto orderDto);


    OrderDto orderToOrderDto(Order order);
}
