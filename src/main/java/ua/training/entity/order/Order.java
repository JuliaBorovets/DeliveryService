package ua.training.entity.order;

import lombok.*;
import ua.training.entity.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "order_service",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services = new ArrayList<>();

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private OrderCheck check;

}


