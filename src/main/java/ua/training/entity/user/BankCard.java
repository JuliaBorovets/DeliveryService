package ua.training.entity.user;

import lombok.*;
import ua.training.entity.order.OrderCheck;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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

    public BigDecimal replenish(BigDecimal balance){
        return this.getBalance().add(balance);
    }

    @PreRemove
    public void deleteBankCard(){
        users.forEach(b -> b.getCards().remove(this));
    }
}
