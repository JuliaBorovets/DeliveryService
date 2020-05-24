package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

@Component
public class DestinationToDtoConverter implements Converter<Destination, DestinationDto> {
    @Override
    public DestinationDto convert(Destination destination) {
        return null;
    }
}
