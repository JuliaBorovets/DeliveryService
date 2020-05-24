package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

@Component
public class DtoToOrderTypeConverter implements Converter<OrderTypeDto, OrderType> {
    @Override
    public OrderType convert(OrderTypeDto orderTypeDto) {
        return null;
    }
}
