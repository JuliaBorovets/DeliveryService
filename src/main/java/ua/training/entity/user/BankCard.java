package ua.training.entity.user;

import lombok.*;
import ua.training.entity.order.OrderCheck;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "card",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class BankCard {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private Long expMonth;

    private Long expYear;

    private Long ccv;

    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToMany(mappedBy = "cards", cascade = CascadeType.REFRESH)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "bankCard")
    private List<OrderCheck> orderChecks = new ArrayList<>();

    @PreRemove
    public void deleteBankCard(){
        users.forEach(b -> b.getCards().remove(this));
        orderChecks.forEach(b -> b.setBankCard(null));
    }

    public void deleteUser(User user){
        users.remove(user);
        user.getCards().remove(this);
    }

}
