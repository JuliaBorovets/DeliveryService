package ua.training.entity.order;

import lombok.*;
import ua.training.entity.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    private OrderType orderType;

    @ManyToOne
    private User owner;

    private BigDecimal weight;

    @ManyToOne
    private Destination destination;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private LocalDate shippingDate;

    private LocalDate deliveryDate;

    private BigDecimal shippingPriceInCents;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private OrderCheck check;

    public Order saveOrder(User user){
        this.setOwner(user);
        user.getOrders().add(this);
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", orderType=" + orderType +
                ", owner=" + owner +
                ", weight=" + weight +
                ", destination=" + destination +
                ", status=" + status +
                ", shippingDate=" + shippingDate +
                ", deliveryDate=" + deliveryDate +
                ", shippingPriceInCents=" + shippingPriceInCents +
                ", check=" + check +
                '}';
    }
}


