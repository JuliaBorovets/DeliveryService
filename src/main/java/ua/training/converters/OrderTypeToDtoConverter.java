package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;

@Component
public class OrderTypeToDtoConverter implements Converter<OrderType, OrderTypeDto> {

    @Synchronized
    @Nullable
    @Override
    public OrderTypeDto convert(OrderType orderType) {

        final OrderTypeDto orderTypeDto = new OrderTypeDto();
        orderTypeDto.setId(orderType.getId());
        orderTypeDto.setName(orderType.getName());
        orderTypeDto.setPriceInCents(orderType.getPriceInCents());

        return orderTypeDto;
    }
}
