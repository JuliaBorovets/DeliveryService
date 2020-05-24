package ua.training.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.Service;

@Component
public class DtoToServiceConverter implements Converter<ServiceDto, Service> {
    @Override
    public Service convert(ServiceDto serviceDto) {
        return null;
    }
}
