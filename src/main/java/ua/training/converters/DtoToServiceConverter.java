package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.Service;

@Component
public class DtoToServiceConverter implements Converter<ServiceDto, Service> {

    @Synchronized
    @Nullable
    @Override
    public Service convert(ServiceDto serviceDto) {

        final Service service = new Service();
        service.setId(serviceDto.getId());
        service.setName(serviceDto.getName());
        service.setPriceInCents(serviceDto.getPriceInCents());

        return service;
    }
}
