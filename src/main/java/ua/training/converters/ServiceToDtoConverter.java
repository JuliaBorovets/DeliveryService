package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.ServiceDto;
import ua.training.entity.order.Service;

@Component
public class ServiceToDtoConverter implements Converter<Service, ServiceDto> {

    @Synchronized
    @Nullable
    @Override
    public ServiceDto convert(Service service) {

        final ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(service.getId());
        serviceDto.setName(service.getName());
        serviceDto.setPriceInCents(service.getPriceInCents());

        return serviceDto;
    }
}
