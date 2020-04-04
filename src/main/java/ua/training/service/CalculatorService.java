package ua.training.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.training.dto.CalculatorDTO;
import ua.training.entity.order.Destination;
import ua.training.entity.order.OrderType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Getter
@Service
public class CalculatorService {

    @Value("${constants.BASE.PRICE}")
    Integer BASE_PRICE;

    @Value("${constant.DOLLAR}")
    BigDecimal DOLLAR;

    @Value("${constants.COEFFICIENT}")
    Double COEFFICIENT;


    private int getDestinationPrice(CalculatorDTO orderDTO) {
        return Destination.valueOf(orderDTO.getCalcDestination()).getPriceForDestination();
    }

    private int getTypePrice(CalculatorDTO orderDTO) {
        return OrderType.valueOf(orderDTO.getCalcType()).getPriceForType();
    }

    public BigDecimal calculatePrice(CalculatorDTO orderDTO) {
        return convertPriceToLocale(BigDecimal.valueOf(+(getDestinationPrice(orderDTO) + getTypePrice(orderDTO))
                * COEFFICIENT));
    }

    private BigDecimal convertPriceToLocale(BigDecimal price) {
        return isLocaleUa() ? price : price.divide(DOLLAR, 2, RoundingMode.HALF_UP);
    }

    private boolean isLocaleUa() {
        return getLocale().equals(new Locale("uk"));
    }


}
