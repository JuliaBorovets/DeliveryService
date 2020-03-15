package ua.training.entity.order;


import javax.persistence.*;
import java.math.BigDecimal;

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

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User ownerId;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;
//
//    @Column(name = "destination")
//    @Enumerated(value = EnumType.STRING)
//    private Destination destination;

    @Column(name = "announced_price")
    private BigDecimal announcedPrice;
//
//    @Column(name = "shipping_date")
//    private LocalDate shippingDate;

//    @Column(name = "is_return_shipping", nullable = false)
//    private boolean isReturnShipping;

}
