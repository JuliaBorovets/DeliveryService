package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.converters.CheckToDtoConverter;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;
import ua.training.repository.OrderCheckRepository;
import ua.training.service.OrderCheckService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderCheckServiceImpl implements OrderCheckService {

    private final OrderCheckRepository orderCheckRepository;
    private final CheckToDtoConverter checkToDtoConverter;

    public OrderCheckServiceImpl(OrderCheckRepository orderCheckRepository, CheckToDtoConverter checkToDtoConverter) {
        this.orderCheckRepository = orderCheckRepository;
        this.checkToDtoConverter = checkToDtoConverter;
    }

    @Override
    public List<OrderCheckDto> showAllChecks() {

        List<OrderCheck> orderChecks = new ArrayList<>();

        orderCheckRepository.findAll()
                .iterator()
                .forEachRemaining(orderChecks::add);

        return orderChecks.stream()
                .map(checkToDtoConverter::convert)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderCheckDto> showChecksByUser(Long userId) {

        return orderCheckRepository
                .findAllByUser_Id(userId).stream()
                .map(checkToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderCheckDto> showChecksForMonthOfYear(LocalDate localDateDto) {

        return orderCheckRepository.findAllByCreationDateAfter(localDateDto).stream()
                .map(checkToDtoConverter::convert).collect(Collectors.toList());

    }

    @Override
    public List<OrderCheckDto> showChecksForYear(LocalDate localDateDto) {
        int year = localDateDto.getYear();
        return orderCheckRepository.findAllByCreationYear(year).stream()
                .map(checkToDtoConverter::convert)
                .collect(Collectors.toList());

    }
}
