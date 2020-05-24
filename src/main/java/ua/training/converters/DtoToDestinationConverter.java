package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

@Component
public class DtoToDestinationConverter implements Converter<DestinationDto, Destination> {
    @Override
    public Destination convert(DestinationDto destinationDto) {
        return null;
    }
}
