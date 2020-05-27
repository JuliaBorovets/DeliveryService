package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

@Component
public class DestinationToDtoConverter implements Converter<Destination, DestinationDto> {

    @Synchronized
    @Nullable
    @Override
    public DestinationDto convert(Destination destination) {

        final DestinationDto destinationDto = new DestinationDto();
        destinationDto.setId(destination.getId());
        destinationDto.setCityFrom(destination.getCityFrom());
        destinationDto.setCityTo(destination.getCityTo());
        destinationDto.setDaysToDeliver(destination.getDaysToDeliver());
        destinationDto.setKilometers(destination.getKilometers());
        destinationDto.setPriceOnCentsForKilometer(destination.getPriceOnCentsForKilometer());

        return destinationDto;
    }
}
