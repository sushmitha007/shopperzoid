package com.stackroute.kafka;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SlrPrice {
    private String sellerEmail;
    private Double productPrice;
    private int productStock;
}
