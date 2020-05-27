package ua.training.service.serviceImpl;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ua.training.dto.OrderDto;
import ua.training.service.CalculatorService;

import java.math.BigDecimal;

@Getter
@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public BigDecimal calculatePrice(OrderDto orderDto) {
        return null;
    }
}
