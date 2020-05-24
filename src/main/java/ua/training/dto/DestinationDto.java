package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DestinationDto {

    private Long id;

    private String cityFrom;

    private String cityTo;

    private Long daysToDeliver;

    private BigDecimal kilometers;

    private BigDecimal priceOnCentsForKilometer;

    private List<OrderDto> orders;
}
