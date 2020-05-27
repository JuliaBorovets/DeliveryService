package ua.training.entity.user;

import lombok.*;
import ua.training.entity.order.OrderCheck;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "card",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class BankCard {

    @Id
    private Long id;

    private BigDecimal balance;

    @ManyToMany(mappedBy = "cards", cascade = CascadeType.REFRESH)
    private List<User> users;

    @OneToMany(mappedBy = "bankCard")
    private List<OrderCheck> orderChecks;

    @PreRemove
    public void deleteBankCard(){
        users.forEach(b -> b.getCards().remove(this));
    }
}
