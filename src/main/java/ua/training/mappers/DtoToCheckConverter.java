package ua.training.mappers;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Component
public class DtoToCheckConverter  implements Converter<OrderCheckDto, OrderCheck> {

    @Synchronized
    @Nullable
    @Override
    public OrderCheck convert(OrderCheckDto orderCheckDto) {
        final OrderCheck orderCheck = new OrderCheck();

        orderCheck.setId(orderCheckDto.getId());
        orderCheck.setPriceInCents(orderCheckDto.getPriceInCents());
        orderCheck.setStatus(orderCheckDto.getStatus());

        return orderCheck;
    }


}
