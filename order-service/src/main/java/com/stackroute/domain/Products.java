package com.stackroute.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**With @Data, Lombok will generate getter and setter methods, toString methods, Equal & Hashcode methods*/
@Data

/**@NoArgsConstructor will generate constructor with no arguments*/
@NoArgsConstructor

/**@AllArgsConstructor will generate constructor with all properties in the class*/
@AllArgsConstructor
@Builder
public class Products {

    private String productName;
    private String productDescription;
    private Double productPrice;
    private Double productFinalPrice;
    private String productImage;
    private String sellerEmail;
    private int productQty;

}
