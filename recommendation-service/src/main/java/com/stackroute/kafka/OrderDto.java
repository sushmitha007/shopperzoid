package com.stackroute.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String buyerEmail;
    private Double rating;
    private List<String> products;
    private long buyerPhone;
    private String deliveryAddress;
    private Date timestamp;
    private Date finishTimestamp;
    private String status;

}
