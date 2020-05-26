package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Component
public class DtoToCheckConverter implements Converter<OrderCheckDto, OrderCheck> {

    private final DtoToUserConverter dtoToUserConverter;
    private final DtoToOrderConverter dtoToOrderConverter;
    private final DtoToBankCardConverter dtoToBankCardConverter;

    public DtoToCheckConverter(DtoToUserConverter dtoToUserConverter, DtoToOrderConverter dtoToOrderConverter,
                               DtoToBankCardConverter dtoToBankCardConverter) {
        this.dtoToUserConverter = dtoToUserConverter;
        this.dtoToOrderConverter = dtoToOrderConverter;
        this.dtoToBankCardConverter = dtoToBankCardConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderCheck convert(OrderCheckDto orderCheckDto) {
        final OrderCheck orderCheck = new OrderCheck();
        orderCheck.setId(orderCheckDto.getId());
        orderCheck.setOrder(dtoToOrderConverter.convert(orderCheckDto.getOrder()));
        orderCheck.setPriceInCents(orderCheckDto.getPriceInCents());
        orderCheck.setStatus(orderCheckDto.getStatus());
        orderCheck.setUser(dtoToUserConverter.convert(orderCheckDto.getUser()));
        orderCheck.setBankCard(dtoToBankCardConverter.convert(orderCheckDto.getBankCard()));

        return orderCheck;
    }
}
