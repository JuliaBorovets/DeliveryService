package ua.training.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.training.controller.exception.DestinationException;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;
import ua.training.mappers.DestinationMapper;
import ua.training.repository.DestinationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DestinationServiceImplTest {

    @Mock
    DestinationRepository destinationRepository;

    @InjectMocks
    DestinationServiceImpl service;

    @Test
    void getAllDestinationDto() {
        Iterable<Destination> destinations = Arrays.asList(Destination.builder().id(1L).build(), Destination.builder().id(2L).build());
        when(destinationRepository.findAll()).thenReturn(destinations);

        List<Destination> destinationList = new ArrayList<>();
        destinations.forEach(destinationList::add);

        List<DestinationDto> destinationDtoList = destinationList.stream()
                .map(DestinationMapper.INSTANCE::destinationToDestinationDto)
                .collect(Collectors.toList());

        List<DestinationDto> result = service.getAllDestinationDto();

        assertEquals(result.size(), destinationDtoList.size());
        assertEquals(result.get(0).getId(), destinationDtoList.get(0).getId());
        assertEquals(result.get(1).getId(), destinationDtoList.get(1).getId());

        verify(destinationRepository).findAll();
    }

    @Test
    void getDestination() throws DestinationException {
        String from = "from";
        String to = "to";

        Optional<Destination> destination = Optional.of(Destination.builder().cityFrom(from).cityTo(to).build());

        when(destinationRepository.findByCityFromAndCityTo(anyString(), anyString())).thenReturn(destination);

        Destination result = service.getDestination(from, to);

        assertEquals(result.getCityFrom(), destination.get().getCityFrom());
        assertEquals(result.getCityTo(), destination.get().getCityTo());

        verify(destinationRepository).findByCityFromAndCityTo(anyString(), anyString());
    }
}