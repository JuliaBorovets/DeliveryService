package ua.training.converters;

import lombok.Synchronized;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderDto;
import ua.training.entity.order.Order;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    private final OrderTypeToDtoConverter orderTypeToDtoConverter;
    private final DestinationToDtoConverter destinationToDtoConverter;
    private final ServiceToDtoConverter serviceToDtoConverter;

    public OrderToDtoConverter(OrderTypeToDtoConverter orderTypeToDtoConverter,
                               DestinationToDtoConverter destinationToDtoConverter,
                               ServiceToDtoConverter serviceToDtoConverter) {
        this.orderTypeToDtoConverter = orderTypeToDtoConverter;
        this.destinationToDtoConverter = destinationToDtoConverter;
        this.serviceToDtoConverter = serviceToDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderDto convert(Order order) {

        final OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderType(orderTypeToDtoConverter.convert(order.getOrderType()));
        orderDto.setWeight(order.getWeight());
        orderDto.setDestination(destinationToDtoConverter.convert(order.getDestination()));
        orderDto.setStatus(order.getStatus());
        orderDto.setShippingPriceInCents(order.getShippingPriceInCents());
        orderDto.setShippingDate(order.getShippingDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(LocaleContextHolder.getLocale())));
        orderDto.setDeliveryDate(order.getDeliveryDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                        .withLocale(LocaleContextHolder.getLocale())));

        if (order.getServices() != null && order.getServices().size() > 0){
            order.getServices()
                    .forEach( service -> orderDto.getServices().add(serviceToDtoConverter.convert(service)));
        }
        return orderDto;
    }


}
