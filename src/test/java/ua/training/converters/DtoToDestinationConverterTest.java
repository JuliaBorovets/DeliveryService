package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DtoToDestinationConverterTest {

    final Long ID = 1L;

    final String CITY_FROM = "City from";

    final String CITY_TO = "City to";

    final Long DAYS = 10L;

    final BigDecimal KILOMETERS = BigDecimal.valueOf(3);

    final BigDecimal PRICE = BigDecimal.valueOf(33);

    DtoToDestinationConverter converter;

    @BeforeEach
    void setUp() {

        converter = new DtoToDestinationConverter();
    }

    @Test
    void convert() {
        DestinationDto destinationDto = DestinationDto.builder()
                .id(ID)
                .cityFrom(CITY_FROM)
                .cityTo(CITY_TO)
                .daysToDeliver(DAYS)
                .kilometers(KILOMETERS)
                .priceOnCentsForKilometer(PRICE).build();

        Destination destination = converter.convert(destinationDto);

        assert destination != null;
        assertEquals(destination.getId(), destinationDto.getId());
        assertEquals(destination.getCityFrom(), destinationDto.getCityFrom());
        assertEquals(destination.getCityTo(), destinationDto.getCityTo());
        assertEquals(destination.getDaysToDeliver(), destinationDto.getDaysToDeliver());
        assertEquals(destination.getPriceOnCentsForKilometer(), destinationDto.getPriceOnCentsForKilometer());
        assertEquals(destination.getKilometers(), destinationDto.getKilometers());
    }
}