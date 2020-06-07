package ua.training.mappers;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.OrderCheckDto;
import ua.training.entity.order.OrderCheck;

@Component
public class CheckToDtoConverter implements Converter<OrderCheck, OrderCheckDto> {

    private final UserToUserDtoConverter userToUserDtoConverter;

    public CheckToDtoConverter(UserToUserDtoConverter userToUserDtoConverter) {
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public OrderCheckDto convert(OrderCheck check) {

        final OrderCheckDto orderCheckDto = new OrderCheckDto();

        orderCheckDto.setId(check.getId());
        orderCheckDto.setOrderId(check.getOrder().getId());
        orderCheckDto.setPriceInCents(check.getPriceInCents());
        orderCheckDto.setStatus(check.getStatus());
        if (check.getBankCard() != null) {
            orderCheckDto.setBankCard(check.getBankCard().getId());
        }
        orderCheckDto.setCreationDate(check.getCreationDate());
        orderCheckDto.setUser(userToUserDtoConverter.convert(check.getUser()));
        return orderCheckDto;
    }
}