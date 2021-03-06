package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;


@Mapper(componentModel = "spring")
public interface OrderTypeMapper {

    OrderTypeMapper INSTANCE = Mappers.getMapper(OrderTypeMapper.class);

    OrderTypeDto orderTypeToOrderTypeDto(OrderType orderType);
}
