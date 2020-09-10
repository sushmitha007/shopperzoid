package com.stackroute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductDataController {

    @Autowired
    ProductService productService;

    @GetMapping(value = "api/v1/product")
    public String getProducts(){
        System.out.println("Making a call to the product application");
        return productService.callProductApplication();
    }
}
