package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Component
public class DtoToOrderConverter implements Converter<OrderDto, Order> {
    @Override
    public Order convert(OrderDto orderDto) {
        return null;
    }
}
