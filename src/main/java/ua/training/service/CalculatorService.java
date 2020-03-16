package ua.training.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ua.training.dto.CalculatorDTO;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.OrderType;
import java.math.BigDecimal;

@Getter
@Service
public class CalculatorService {

    private final int BASE_PRICE = 20;
    private final double COEFFICIENT = 1.1;
    private final double COEFFICIENT_FOR_ANN_PRICE = 0.1;


    private int getDestinationPrice(CalculatorDTO orderDTO) {
        return Destination.valueOf(orderDTO.getCalcDestination()).getPriceForDestination();
    }


    private int getTypePrice(CalculatorDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getCalcType()).getPriceForType();
    }

//    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
//        return BigDecimal.valueOf(BASE_PRICE + (getDestinationPrice(orderDTO) + getTypePrice(orderDTO)) * COEFFICIENT +
//                orderDTO.getDtoAnnouncedPrice().doubleValue() * COEFFICIENT_FOR_ANN_PRICE);
//    }

    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
        return BigDecimal.valueOf(BASE_PRICE + (getDestinationPrice(orderDTO) + getTypePrice(orderDTO)) * COEFFICIENT);
    }
}
