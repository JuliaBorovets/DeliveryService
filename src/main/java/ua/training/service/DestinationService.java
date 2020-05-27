package ua.training.service;

import ua.training.dto.DestinationDto;

import java.util.List;

public interface DestinationService {

    List<DestinationDto> getAllDestinationDto();

    DestinationDto getDtoDestination(String cityFrom, String cityTo);

}
