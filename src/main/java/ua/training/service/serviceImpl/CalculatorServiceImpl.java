package ua.training.service.serviceImpl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.training.service.CalculatorService;

@Getter
@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Value("${constants.BASE.PRICE}")
    Integer BASE_PRICE;

    @Value("${constants.COEFFICIENT}")
    Double COEFFICIENT;

    private final ConverterServiceImpl converterService;

    public CalculatorServiceImpl(ConverterServiceImpl converterService) {
        this.converterService = converterService;
    }

//    private int getDestinationPrice(CalculatorDTO orderDTO) {
//        return Destination.valueOf(orderDTO.getCalcDestination()).getPriceForDestination();
//    }
//
//    private int getTypePrice(CalculatorDTO orderDTO) {
//        return OrderType.valueOf(orderDTO.getCalcType()).getPriceForType();
//    }

//    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
//        return converterService.convertPriceToLocale(BigDecimal.valueOf(+(getDestinationPrice(orderDTO) + getTypePrice(orderDTO))
//                * COEFFICIENT), localeName());
//    }

    private String localeName() {
        return LocaleContextHolder.getLocale().toString();
    }

}
