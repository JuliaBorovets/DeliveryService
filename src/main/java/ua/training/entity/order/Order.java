package ua.training.entity.order;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;
import ua.training.entity.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_type")
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "destination")
    @Enumerated(value = EnumType.STRING)
    private Destination destination;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "shippingPrice", nullable = false)
    private BigDecimal shippingPrice;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

}

