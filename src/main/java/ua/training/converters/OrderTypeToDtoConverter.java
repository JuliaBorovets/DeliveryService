package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

@Component
public class OrderTypeToDtoConverter implements Converter<OrderType, OrderTypeDto> {
    @Override
    public OrderTypeDto convert(OrderType orderType) {
        return null;
    }
}
