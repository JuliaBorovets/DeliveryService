package ua.training.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class StatisticsDto {

    private BigDecimal earningsLastMonth;

    private BigDecimal earningsYear;

    private Long deliversNumber;

    private Long deliversNumberYear;

}
