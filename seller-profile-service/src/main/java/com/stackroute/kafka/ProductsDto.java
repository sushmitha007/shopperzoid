package com.stackroute.kafka;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductsDto {
    private String productName;
    private String productDescription;
    private String productImage;
    private String productBrandName;
    private List<SlrPrice> sellPrice;
    private String productCategory;
    private String productSubCategory;
}
