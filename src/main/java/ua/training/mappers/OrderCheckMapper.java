package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Mapper(uses = {OrderMapper.class, UserMapper.class, BankCardMapper.class}, componentModel = "spring")
public interface OrderCheckMapper {

    OrderCheckMapper INSTANCE = Mappers.getMapper(OrderCheckMapper.class);

    OrderCheck orderCheckDtoToOrderCheck(OrderCheckDto orderCheckDto);

    OrderCheckDto orderCheckToOrderCheckDto(OrderCheck orderCheck);
}
