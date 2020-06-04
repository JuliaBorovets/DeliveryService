package ua.training.mappers;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderType;
import ua.training.service.DestinationService;
import ua.training.service.OrderTypeService;

import java.time.LocalDate;

@Component
public class OrderDtoToOrderConverter implements Converter<OrderDto, Order> {

    private final OrderCheckMapper orderCheckMapper;
    private final OrderTypeService orderTypeService;
    private final DestinationService destinationService;

    public OrderDtoToOrderConverter(OrderCheckMapper orderCheckMapper, OrderTypeService orderTypeService, DestinationService destinationService) {
        this.orderCheckMapper = orderCheckMapper;
        this.orderTypeService = orderTypeService;
        this.destinationService = destinationService;
    }


    @Override
    public Order convert(OrderDto orderDto) {

        Order order = Order.builder()
                .id(orderDto.getId())
                .description(orderDto.getDescription())
                .orderType(getOrderType(orderDto.getType()))
                .weight(orderDto.getWeight())
                .status(orderDto.getStatus())
                .shippingPriceInCents(orderDto.getShippingPriceInCents())
                .check(orderCheckMapper.orderCheckDtoToOrderCheck(orderDto.getCheck()))
                .destination(getDestination(orderDto.getDestinationCityFrom(), orderDto.getDestinationCityTo())).build();;


        if (orderDto.getShippingDate() != null) {
            order.setShippingDate(LocalDate.parse(orderDto.getShippingDate()));
        }
        if (orderDto.getDeliveryDate() != null) {
            order.setDeliveryDate(LocalDate.parse(orderDto.getDeliveryDate()));
        }

        return order;

    }

    private OrderType getOrderType(String type){
        return orderTypeService.getOrderTypeById(Long.valueOf(type));
    }

    private Destination getDestination(String from, String to){
        return destinationService.getDestination(from, to);
    }
    
}
