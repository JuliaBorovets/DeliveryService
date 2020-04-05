package ua.training.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class ConverterService {

    @Value("${constant.DOLLAR}")
    BigDecimal DOLLAR;

    public BigDecimal convertPriceFromLocale(BigDecimal price, String locale) {
        return locale.equals("uk") ? price : price.multiply(DOLLAR);
    }

    public BigDecimal convertPriceToLocale(BigDecimal price, String locale) {
        return locale.equals("en") ? price.divide(DOLLAR, 2, RoundingMode.HALF_UP) : price;
    }

}
