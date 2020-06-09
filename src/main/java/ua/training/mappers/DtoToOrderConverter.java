package ua.training.mappers;

import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.controller.exception.DestinationException;
import ua.training.controller.exception.OrderTypeException;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Destination;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderType;
import ua.training.service.DestinationService;
import ua.training.service.OrderTypeService;

import java.time.LocalDate;

@Component
public class DtoToOrderConverter implements Converter<OrderDto, Order> {

    private final OrderTypeService orderTypeService;
    private final DestinationService destinationService;

    public DtoToOrderConverter(OrderTypeService orderTypeService, DestinationService destinationService) {
        this.orderTypeService = orderTypeService;
        this.destinationService = destinationService;
    }

    @Nullable
    @Synchronized
    @Override
    public Order convert(OrderDto orderDto) {

        final Order order = new Order();
        order.setId(orderDto.getId());
        order.setDescription(orderDto.getDescription());
        order.setWeight(orderDto.getWeight());
        order.setStatus(orderDto.getStatus());
        order.setShippingPriceInCents(orderDto.getShippingPriceInCents());
        if (orderDto.getShippingDate() != null) {
            order.setShippingDate(LocalDate.parse(orderDto.getShippingDate()));
        }
        if (orderDto.getDeliveryDate() != null) {
            order.setDeliveryDate(LocalDate.parse(orderDto.getDeliveryDate()));
        }

        try {
            order.setOrderType(getOrderType(orderDto.getType()));
            order.setDestination(getDestination(orderDto.getDestinationCityFrom(), orderDto.getDestinationCityTo()));
        } catch (OrderTypeException | DestinationException e) {
            e.printStackTrace();
        }

        return order;
    }

    private OrderType getOrderType(String type) throws OrderTypeException {
        return orderTypeService.getOrderTypeById(Long.valueOf(type));
    }

    private Destination getDestination(String from, String to) throws DestinationException {
        return destinationService.getDestination(from, to);
    }
    
}
