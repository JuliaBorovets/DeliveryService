package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

@Component
public class DtoToOrderTypeConverter implements Converter<OrderTypeDto, OrderType> {

    @Synchronized
    @Nullable
    @Override
    public OrderType convert(OrderTypeDto orderTypeDto) {

        final OrderType orderType = new OrderType();
        orderType.setId(orderTypeDto.getId());
        orderType.setName(orderTypeDto.getName());
        orderType.setPriceInCents(orderTypeDto.getPriceInCents());

        return orderType;
    }
}
