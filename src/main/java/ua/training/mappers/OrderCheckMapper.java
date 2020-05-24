package ua.training.mappers;

import org.mapstruct.Mapper;
import ua.training.dto.OrderCheckDTO;
import ua.training.entity.order.OrderCheck;

@Mapper
public interface OrderCheckMapper {

    OrderCheck checkDtoToCheck(OrderCheckDTO checkDTO);

    OrderCheckDTO checkToCheckDto(OrderCheck check);
}
