package ua.training.service;

import java.math.BigDecimal;

public interface ConverterService {

    BigDecimal convertPriceFromLocale(BigDecimal price, String locale);

    BigDecimal convertPriceToLocale(BigDecimal price, String locale);

}
