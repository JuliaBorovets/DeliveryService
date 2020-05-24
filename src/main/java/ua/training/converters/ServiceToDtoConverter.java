package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.Service;

@Component
public class ServiceToDtoConverter implements Converter<Service, ServiceDto> {
    @Override
    public ServiceDto convert(Service service) {
        return null;
    }
}
