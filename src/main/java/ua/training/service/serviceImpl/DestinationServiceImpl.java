package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.converters.DestinationToDtoConverter;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;
import ua.training.repository.DestinationRepository;
import ua.training.service.DestinationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationToDtoConverter destinationToDtoConverter;

    public DestinationServiceImpl(DestinationRepository destinationRepository,
                                  DestinationToDtoConverter destinationToDtoConverter) {
        this.destinationRepository = destinationRepository;
        this.destinationToDtoConverter = destinationToDtoConverter;
    }

    @Override
    public List<DestinationDto> getAllDestinationDto() {

        List<Destination> destinations = new ArrayList<>();
        destinationRepository.findAll().forEach(destinations::add);

        return destinations.stream()
                .map(destinationToDtoConverter::convert)
                .collect(Collectors.toList());
    }


    @Override
    public DestinationDto getDtoDestination(String cityFrom, String cityTo) {

        Destination destination = destinationRepository
                .findByCityFromAndCityTo(cityFrom, cityTo)
                .orElseThrow(() -> new RuntimeException("can not find destination"));

        return destinationToDtoConverter.convert(destination);
    }

}
