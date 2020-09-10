package com.stackroute.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String orderId;
    private String buyerEmail;
    private Double rating;
    private List<Products> products;
    private long buyerPhone;
    private String deliveryAddress;
    private Date timestamp;
    private Date finishTimestamp;
    private String status;
}
