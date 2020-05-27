package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

import java.time.LocalDate;

@Component
public class DtoToOrderConverter implements Converter<OrderDto, Order> {

    private final DtoToOrderTypeConverter dtoToOrderTypeConverter;
    private final DtoToDestinationConverter dtoToDestinationConverter;
    private final DtoToServiceConverter dtoToServiceConverter;

    public DtoToOrderConverter(DtoToOrderTypeConverter dtoToOrderTypeConverter,
                               DtoToDestinationConverter dtoToDestinationConverter,
                               DtoToServiceConverter dtoToServiceConverter) {
        this.dtoToOrderTypeConverter = dtoToOrderTypeConverter;
        this.dtoToDestinationConverter = dtoToDestinationConverter;
        this.dtoToServiceConverter = dtoToServiceConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Order convert(OrderDto orderDto) {
        final Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderType(dtoToOrderTypeConverter.convert(orderDto.getOrderType()));
        order.setWeight(orderDto.getWeight());
        order.setDestination(dtoToDestinationConverter.convert(orderDto.getDestination()));
        order.setStatus(orderDto.getStatus());
        order.setShippingDate(LocalDate.now());

        if (orderDto.getServices() != null && orderDto.getServices().size() > 0){
            orderDto.getServices()
                    .forEach( service -> order.getServices().add(dtoToServiceConverter.convert(service)));
        }
        return order;
    }
}
