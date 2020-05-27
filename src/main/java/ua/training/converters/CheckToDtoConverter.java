package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Component
public class CheckToDtoConverter implements Converter<OrderCheck, OrderCheckDto> {

    private final UserToUserDtoConverter userToUserDtoConverter;
    private final OrderToDtoConverter orderToDtoConverter;
    private final BankCardToDtoConverter bankCardToDtoConverter;

    public CheckToDtoConverter(UserToUserDtoConverter userToUserDtoConverter, OrderToDtoConverter orderToDtoConverter,
                               BankCardToDtoConverter bankCardToDtoConverter) {
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.orderToDtoConverter = orderToDtoConverter;
        this.bankCardToDtoConverter = bankCardToDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderCheckDto convert(OrderCheck check) {
        final OrderCheckDto orderCheckDto = new OrderCheckDto();
        orderCheckDto.setId(check.getId());
        orderCheckDto.setOrder(orderToDtoConverter.convert(check.getOrder()));
        orderCheckDto.setPriceInCents(check.getPriceInCents());
        orderCheckDto.setStatus(check.getStatus());
        orderCheckDto.setUser(userToUserDtoConverter.convert(check.getUser()));
        orderCheckDto.setBankCard(bankCardToDtoConverter.convert(check.getBankCard()));

        return orderCheckDto;
    }
}
