package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceDto {

    private Long id;

    private String name;

    private BigDecimal priceInCents;

    private Set<OrderDto> orders;
}
