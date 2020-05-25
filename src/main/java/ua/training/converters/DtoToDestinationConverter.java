package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

@Component
public class DtoToDestinationConverter implements Converter<DestinationDto, Destination> {

    @Synchronized
    @Nullable
    @Override
    public Destination convert(DestinationDto destinationDto) {

        final Destination destination = new Destination();
        destination.setId(destinationDto.getId());
        destination.setCityFrom(destinationDto.getCityFrom());
        destination.setCityTo(destinationDto.getCityTo());
        destination.setDaysToDeliver(destinationDto.getDaysToDeliver());
        destination.setKilometers(destinationDto.getKilometers());
        destination.setPriceOnCentsForKilometer(destinationDto.getPriceOnCentsForKilometer());

        return destination;
    }
}
