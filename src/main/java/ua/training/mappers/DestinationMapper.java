package ua.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.training.dto.DestinationDto;
import ua.training.entity.order.Destination;

@Mapper(componentModel = "spring")
public interface  DestinationMapper {

    DestinationMapper INSTANCE = Mappers.getMapper(DestinationMapper.class);

    DestinationDto destinationToDestinationDto(Destination destination);

    Destination destinationDtoToDestination(DestinationDto destinationDto);

}
