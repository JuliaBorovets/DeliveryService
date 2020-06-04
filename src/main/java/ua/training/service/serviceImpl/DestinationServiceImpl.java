package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;
import ua.training.mappers.DestinationMapper;
import ua.training.repository.DestinationRepository;
import ua.training.service.DestinationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public List<DestinationDto> getAllDestinationDto() {

        List<Destination> destinations = new ArrayList<>();
        destinationRepository.findAll().forEach(destinations::add);

        return destinations.stream()
                .map(DestinationMapper.INSTANCE::destinationToDestinationDto)
                .collect(Collectors.toList());
    }


    @Override
    public Destination getDestination(String cityFrom, String cityTo) {

        return destinationRepository
                .findByCityFromAndCityTo(cityFrom, cityTo)
                .orElseThrow(() -> new RuntimeException("can not find destination"));
    }

}
