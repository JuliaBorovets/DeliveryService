package ua.training.service.serviceImpl;

import org.springframework.stereotype.Service;
import ua.training.controller.exception.OrderTypeException;
import ua.training.dto.OrderTypeDto;
import ua.training.entity.order.OrderType;
import ua.training.mappers.OrderTypeMapper;
import ua.training.repository.OrderTypeRepository;
import ua.training.service.OrderTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTypeServiceImpl implements OrderTypeService {

    private final OrderTypeRepository orderTypeRepository;

    public OrderTypeServiceImpl(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }

    @Override
    public List<OrderTypeDto> getAllOrderTypeDto() {

        List<OrderType> dtoList = new ArrayList<>();
        orderTypeRepository.findAll()
                .iterator().forEachRemaining(dtoList::add);

        return dtoList.stream()
                .map(OrderTypeMapper.INSTANCE::orderTypeToOrderTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderType getOrderTypeById(Long id) throws OrderTypeException {
        return orderTypeRepository.findById(id)
                .orElseThrow(()->new OrderTypeException("no type with id = " + id));
    }
}
