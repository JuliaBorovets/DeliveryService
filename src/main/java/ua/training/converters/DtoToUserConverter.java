package ua.training.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.training.controller.utility.ProjectPasswordEncoder;
import ua.training.dto.UserDto;
import ua.training.entity.user.RoleType;
import ua.training.entity.user.User;

@Component
public class DtoToUserConverter implements Converter<UserDto, User> {

    private final DtoToOrderConverter dtoToOrderConverter;
    private final DtoToCheckConverter dtoToCheckConverter;
    private final DtoToBankCardConverter dtoToBankCardConverter;
    private final ProjectPasswordEncoder passwordEncoder;

    public DtoToUserConverter(DtoToOrderConverter dtoToOrderConverter, DtoToCheckConverter dtoToCheckConverter,
                              DtoToBankCardConverter dtoToBankCardConverter, ProjectPasswordEncoder passwordEncoder) {
        this.dtoToOrderConverter = dtoToOrderConverter;
        this.dtoToCheckConverter = dtoToCheckConverter;
        this.dtoToBankCardConverter = dtoToBankCardConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Synchronized
    @Nullable
    @Override
    public User convert(UserDto userDto) {

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setFirstNameCyr(userDto.getFirstNameCyr());
        user.setLastNameCyr(userDto.getLastNameCyr());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setRole(RoleType.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        if (userDto.getOrders() != null && userDto.getOrders().size() > 0){
            userDto.getOrders()
                    .forEach( order -> user.getOrders().add(dtoToOrderConverter.convert(order)));
        }

        if (userDto.getChecks() != null && userDto.getChecks().size() > 0){
            userDto.getChecks()
                    .forEach( check -> user.getChecks().add(dtoToCheckConverter.convert(check)));
        }

        if (userDto.getCards() != null && userDto.getCards().size() > 0){
            userDto.getCards()
                    .forEach( card -> user.getCards().add(dtoToBankCardConverter.convert(card)));
        }


        return user;
    }
}
