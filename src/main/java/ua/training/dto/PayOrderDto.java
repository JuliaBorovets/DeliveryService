package ua.training.dto;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class PayOrderDto {

    private Long bankCardId;

    private Long orderId;

}
