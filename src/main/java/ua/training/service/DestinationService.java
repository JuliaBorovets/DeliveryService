package ua.training.service;

import ua.training.controller.exception.DestinationException;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

import java.util.List;

public interface DestinationService {

    List<DestinationDto> getAllDestinationDto();

    Destination getDestination(String cityFrom, String cityTo) throws DestinationException;

}
