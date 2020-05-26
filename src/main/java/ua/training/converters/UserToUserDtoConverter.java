package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.dto.UserDto;
import ua.training.entity.user.User;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    private final OrderToDtoConverter orderToDtoConverter;
    private final CheckToDtoConverter checkToDtoConverter;
    private final BankCardToDtoConverter bankCardToDtoConverter;

    public UserToUserDtoConverter(OrderToDtoConverter orderToDtoConverter, CheckToDtoConverter checkToDtoConverter,
                                  BankCardToDtoConverter bankCardToDtoConverter) {
        this.orderToDtoConverter = orderToDtoConverter;
        this.checkToDtoConverter = checkToDtoConverter;
        this.bankCardToDtoConverter = bankCardToDtoConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public UserDto convert(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setFirstNameCyr(user.getFirstNameCyr());
        userDto.setLastNameCyr(user.getLastNameCyr());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        if (user.getOrders() != null && user.getOrders().size() > 0){
            user.getOrders()
                    .forEach( order -> userDto.getOrders().add(orderToDtoConverter.convert(order)));
        }

        if (user.getChecks() != null && user.getChecks().size() > 0){
            user.getChecks()
                    .forEach( check -> userDto.getChecks().add(checkToDtoConverter.convert(check)));
        }

        if (user.getCards() != null && user.getCards().size() > 0){
            user.getCards()
                    .forEach( card -> userDto.getCards().add(bankCardToDtoConverter.convert(card)));
        }

        return userDto;
    }
}
