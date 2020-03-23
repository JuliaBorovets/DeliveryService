package ua.training.dto;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class AddMoneyDTO {

    private Long accountId;
    private Long amount;
}
