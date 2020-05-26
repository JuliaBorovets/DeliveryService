package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Component
public class DtoToOrderConverter implements Converter<OrderDto, Order> {

    private final DtoToOrderTypeConverter dtoToOrderTypeConverter;
    private final DtoToUserConverter dtoToUserConverter;
    private final DtoToDestinationConverter dtoToDestinationConverter;
    private final DtoToServiceConverter dtoToServiceConverter;
    private final DtoToCheckConverter dtoToCheckConverter;

    public DtoToOrderConverter(DtoToOrderTypeConverter dtoToOrderTypeConverter, DtoToUserConverter dtoToUserConverter,
                               DtoToDestinationConverter dtoToDestinationConverter,
                               DtoToServiceConverter dtoToServiceConverter, DtoToCheckConverter dtoToCheckConverter) {
        this.dtoToOrderTypeConverter = dtoToOrderTypeConverter;
        this.dtoToUserConverter = dtoToUserConverter;
        this.dtoToDestinationConverter = dtoToDestinationConverter;
        this.dtoToServiceConverter = dtoToServiceConverter;
        this.dtoToCheckConverter = dtoToCheckConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Order convert(OrderDto orderDto) {
        final Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderType(dtoToOrderTypeConverter.convert(orderDto.getOrderType()));
        order.setOwner(dtoToUserConverter.convert(orderDto.getOwner()));
        order.setWeight(orderDto.getWeight());
        order.setDestination(dtoToDestinationConverter.convert(orderDto.getDestination()));
        order.setStatus(orderDto.getStatus());
        order.setShippingDate(orderDto.getShippingDate());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setCheck(dtoToCheckConverter.convert(orderDto.getCheck()));

        if (orderDto.getServices() != null && orderDto.getServices().size() > 0){
            orderDto.getServices()
                    .forEach( service -> order.getServices().add(dtoToServiceConverter.convert(service)));
        }
        return order;
    }
}
