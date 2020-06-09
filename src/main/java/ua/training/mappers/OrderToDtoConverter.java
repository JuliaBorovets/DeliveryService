package ua.training.mappers;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    private final OrderTypeMapper orderTypeMapper;
    private final DestinationMapper destinationMapper;
    private final CheckToDtoConverter checkToDtoConverter;

    public OrderToDtoConverter(OrderTypeMapper orderTypeMapper, DestinationMapper destinationMapper,
                               CheckToDtoConverter checkToDtoConverter) {
        this.orderTypeMapper = orderTypeMapper;
        this.destinationMapper = destinationMapper;
        this.checkToDtoConverter = checkToDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderDto convert(Order order) {

        final OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderType(orderTypeMapper.orderTypeToOrderTypeDto(order.getOrderType()));
        orderDto.setWeight(order.getWeight());
        orderDto.setDestination(destinationMapper.destinationToDestinationDto(order.getDestination()));
        orderDto.setStatus(order.getStatus());
        orderDto.setShippingDate(order.getShippingDate().toString());
        orderDto.setShippingPriceInCents(order.getShippingPriceInCents());
        orderDto.setDescription(order.getDescription());
        if (order.getDeliveryDate() != null) {
            orderDto.setDeliveryDate(order.getDeliveryDate().toString());
        }
        if (order.getCheck() != null){
            orderDto.setCheck(checkToDtoConverter.convert(order.getCheck()));
        }
        return orderDto;
    }
}