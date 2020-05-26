package ua.training.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.dto.DestinationDto;
import ua.training.dto.OrderDto;
import ua.training.dto.OrderTypeDto;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderToDtoConverterTest {

    @Mock
    OrderTypeToDtoConverter orderTypeToDtoConverter;

    @Mock
    DestinationToDtoConverter destinationToDtoConverter;

    @Mock
    ServiceToDtoConverter serviceToDtoConverter;

    @InjectMocks
    OrderToDtoConverter converter;

    final Long ORDER_ID = 11L;
    final Long USER_ID = 1L;
    final Long SERVICE_ID = 2L;
    final Long CHECK_ID = 3L;
    final Long DESTINATION_ID = 4L;
    final Long ORDER_TYPE_ID = 5L;

    @Test
    void convert() {

        ServiceDto serviceDto = ServiceDto.builder().id(SERVICE_ID).build();
        DestinationDto destinationDto = DestinationDto.builder().id(DESTINATION_ID).build();
        OrderTypeDto orderTypeDto = OrderTypeDto.builder().id(ORDER_TYPE_ID).build();

        Order order = new Order();
        order.setId(ORDER_ID);
        order.getServices().add(Service.builder().id(SERVICE_ID).build());
        order.setCheck(OrderCheck.builder().id(CHECK_ID).build());
        order.setDestination(Destination.builder().id(DESTINATION_ID).build());
        order.setOrderType(OrderType.builder().id(ORDER_TYPE_ID).build());

        when(serviceToDtoConverter.convert(any(Service.class))).thenReturn(serviceDto);
        when(destinationToDtoConverter.convert(any(Destination.class))).thenReturn(destinationDto);
        when(orderTypeToDtoConverter.convert(any(OrderType.class))).thenReturn(orderTypeDto);

        OrderDto orderDto = converter.convert(order);

        assert orderDto != null;
        assertEquals(orderDto.getId(), order.getId());
        assertEquals(orderDto.getServices().size(), order.getServices().size());
        assertEquals(orderDto.getOrderType().getId(), order.getOrderType().getId());
        assertEquals(orderDto.getDestination().getId(), order.getDestination().getId());

        verify(serviceToDtoConverter, times(1)).convert(any(Service.class));
        verify(destinationToDtoConverter, times(1)).convert(any(Destination.class));
        verify(orderTypeToDtoConverter, times(1)).convert(any(OrderType.class));
    }
}