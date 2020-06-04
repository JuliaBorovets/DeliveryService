package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

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

    private BigDecimal priceInCents;
}
