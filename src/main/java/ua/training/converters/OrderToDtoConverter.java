package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    private final OrderTypeToDtoConverter orderTypeToDtoConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final DestinationToDtoConverter destinationToDtoConverter;
    private final ServiceToDtoConverter serviceToDtoConverter;
    private final CheckToDtoConverter checkToDtoConverter;

    public OrderToDtoConverter(OrderTypeToDtoConverter orderTypeToDtoConverter,
                               UserToUserDtoConverter userToUserDtoConverter,
                               DestinationToDtoConverter destinationToDtoConverter,
                               ServiceToDtoConverter serviceToDtoConverter, CheckToDtoConverter checkToDtoConverter) {
        this.orderTypeToDtoConverter = orderTypeToDtoConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.destinationToDtoConverter = destinationToDtoConverter;
        this.serviceToDtoConverter = serviceToDtoConverter;
        this.checkToDtoConverter = checkToDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderDto convert(Order order) {

        final OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderType(orderTypeToDtoConverter.convert(order.getOrderType()));
        orderDto.setOwner(userToUserDtoConverter.convert(order.getOwner()));
        orderDto.setWeight(order.getWeight());
        orderDto.setDestination(destinationToDtoConverter.convert(order.getDestination()));
        orderDto.setStatus(order.getStatus());
        orderDto.setShippingDate(order.getShippingDate());
        orderDto.setDeliveryDate(order.getDeliveryDate());
        orderDto.setCheck(checkToDtoConverter.convert(order.getCheck()));

        if (order.getServices() != null && order.getServices().size() > 0){
            order.getServices()
                    .forEach( service -> orderDto.getServices().add(serviceToDtoConverter.convert(service)));
        }
        return orderDto;
    }
}
