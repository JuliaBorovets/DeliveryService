package ua.training.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.training.dto.CalculatorDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.OrderType;

import java.math.BigDecimal;

@Getter
@Service
public class CalculatorService {

    @Value("${constants.BASE.PRICE}")
    Integer BASE_PRICE;

    @Value("${constants.COEFFICIENT}")
    Double COEFFICIENT;

    private ConverterService converterService;

    @Autowired
    public CalculatorService(ConverterService converterService) {
        this.converterService = converterService;
    }

    private int getDestinationPrice(CalculatorDTO orderDTO) {
        return Destination.valueOf(orderDTO.getCalcDestination()).getPriceForDestination();
    }

    private int getTypePrice(CalculatorDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getCalcType()).getPriceForType();
    }

    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
        return converterService.convertPriceToLocale(BigDecimal.valueOf(+(getDestinationPrice(orderDTO) + getTypePrice(orderDTO))
                * COEFFICIENT), localeName());
    }

    private String localeName() {
        return LocaleContextHolder.getLocale().toString();
    }

}
