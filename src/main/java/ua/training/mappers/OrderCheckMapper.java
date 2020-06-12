package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Mapper(uses = UserMapper.class, componentModel = "spring")
public interface OrderCheckMapper {

    OrderCheckMapper INSTANCE = Mappers.getMapper(OrderCheckMapper.class);

    @Mappings({
            @Mapping(target = "orderId", source = "orderCheck.order.id"),
            @Mapping(target = "bankCard", source = "orderCheck.bankCard.id"),
            @Mapping(target = "userId", source = "orderCheck.user.id")

    })
    OrderCheckDto orderCheckToOrderCheckDto(OrderCheck orderCheck);
}
