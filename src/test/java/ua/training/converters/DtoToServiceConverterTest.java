package ua.training.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.Service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DtoToServiceConverterTest {

    DtoToServiceConverter converter;

    final Long ID = 1L;
    final String NAME = "converter";
    final BigDecimal PRICE = BigDecimal.valueOf(50);

    @BeforeEach
    void setUp() {
        converter = new DtoToServiceConverter();
    }

    @Test
    void convert() {
        ServiceDto serviceDto = ServiceDto.builder().id(ID).name(NAME).priceInCents(PRICE).build();

        Service service = converter.convert(serviceDto);

        assert serviceDto != null;
        assertEquals(service.getId(), serviceDto.getId());
        assertEquals(service.getName(), serviceDto.getName());
        assertEquals(service.getPriceInCents(), serviceDto.getPriceInCents());
    }
}