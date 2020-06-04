package ua.training.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DestinationMapperImpl.class)
class DestinationMapperTest {

    final Long ID = 1L;

    final String CITY_FROM = "City from";

    final String CITY_TO = "City to";

    final Long DAYS = 10L;

    final BigDecimal KILOMETERS = BigDecimal.valueOf(3);

    final BigDecimal PRICE = BigDecimal.valueOf(33);

    @Autowired
    DestinationMapper mapper;

    @Test
    void destinationToDestinationDto() {
        Destination destination = Destination.builder()
                .id(ID)
                .cityFrom(CITY_FROM)
                .cityTo(CITY_TO)
                .daysToDeliver(DAYS)
             //   .kilometers(KILOMETERS)
              //  .priceOnCentsForKilometer(PRICE)
                .build();

        DestinationDto destinationDto = mapper.destinationToDestinationDto(destination);

        assert destinationDto != null;
        assertEquals(destination.getId(), destinationDto.getId());
        assertEquals(destination.getCityFrom(), destinationDto.getCityFrom());
        assertEquals(destination.getCityTo(), destinationDto.getCityTo());
        assertEquals(destination.getDaysToDeliver(), destinationDto.getDaysToDeliver());
//        assertEquals(destination.getPriceOnCentsForKilometer(), destinationDto.getPriceOnCentsForKilometer());
//        assertEquals(destination.getKilometers(), destinationDto.getKilometers());
    }

    @Test
    void destinationDtoToDestination() {
        DestinationDto destinationDto = DestinationDto.builder()
                .id(ID)
                .cityFrom(CITY_FROM)
                .cityTo(CITY_TO)
                .daysToDeliver(DAYS)
               // .kilometers(KILOMETERS)
                //.priceOnCentsForKilometer(PRICE)
                .build();

        Destination destination = mapper.destinationDtoToDestination(destinationDto);

        assert destination != null;
        assertEquals(destination.getId(), destinationDto.getId());
        assertEquals(destination.getCityFrom(), destinationDto.getCityFrom());
        assertEquals(destination.getCityTo(), destinationDto.getCityTo());
        assertEquals(destination.getDaysToDeliver(), destinationDto.getDaysToDeliver());
       // assertEquals(destination.getPriceOnCentsForKilometer(), destinationDto.getPriceOnCentsForKilometer());
       // assertEquals(destination.getKilometers(), destinationDto.getKilometers());
    }
}