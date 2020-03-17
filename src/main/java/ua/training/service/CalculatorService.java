package ua.training.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ua.training.dto.CalculatorDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.OrderType;
import java.math.BigDecimal;

@Getter
@Service
public class CalculatorService {

    private int getDestinationPrice(CalculatorDTO orderDTO) {
        return Destination.valueOf(orderDTO.getCalcDestination()).getPriceForDestination();
    }

    private int getTypePrice(CalculatorDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getCalcType()).getPriceForType();
    }

    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
        return BigDecimal.valueOf(ShipmentsTariffs.BASE_PRICE + (getDestinationPrice(orderDTO) + getTypePrice(orderDTO))
                * ShipmentsTariffs.COEFFICIENT + orderDTO.getCalcAnnPrice().doubleValue() *
                ShipmentsTariffs.COEFFICIENT_FOR_ANN_PRICE);
    }

}
