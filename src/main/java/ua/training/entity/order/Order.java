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

    //TODO  коритсувач

    //TODO  отримувач

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    //TODO об'ємна вага

    @Column(name = "destination")
    @Enumerated(value = EnumType.STRING)
    private Destination destination;

    @Column(name = "price")
    private BigDecimal price;


    //TODO чи є зворотня доставка

    //TODO дата доставки

    //TODO оголошена вартість


}
