package ua.training.entity.order;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "type",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class OrderType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal priceInCents;

    @OneToMany(mappedBy = "orderType")
    private List<Order> orderSet;

}
