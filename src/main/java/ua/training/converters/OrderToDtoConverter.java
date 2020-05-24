package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {
    @Override
    public OrderDto convert(Order order) {
        return null;
    }
}
