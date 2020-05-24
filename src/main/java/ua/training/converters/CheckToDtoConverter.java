package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Component
public class CheckToDtoConverter implements Converter<OrderCheck, OrderCheckDto> {
    @Override
    public OrderCheckDto convert(OrderCheck check) {
        return null;
    }
}
