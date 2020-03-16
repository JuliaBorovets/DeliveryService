package ua.training.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
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


    private int getDestinationPrice(OrderDTO orderDTO) {
        return Destination.valueOf(orderDTO.getDtoDestination()).getPriceForDestination();
    }


    private int getTypePrice(OrderDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getDtoOrderType()).getPriceForType();
    }

    public BigDecimal calculatePrice(OrderDTO orderDTO) {
        return BigDecimal.valueOf(BASE_PRICE + (getDestinationPrice(orderDTO) + getTypePrice(orderDTO)) * COEFFICIENT +
                orderDTO.getDtoAnnouncedPrice().doubleValue() * COEFFICIENT_FOR_ANN_PRICE);
    }
}
