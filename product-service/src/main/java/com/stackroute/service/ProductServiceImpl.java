package com.stackroute.service;

import com.stackroute.domain.Product;
import com.stackroute.domain.Seller;
import com.stackroute.exception.ProductAlreadyExistsException;
import com.stackroute.exception.ProductNotExistsException;
import com.stackroute.kafka.NewSellerDto;
import com.stackroute.kafka.ProductsDto;
import com.stackroute.kafka.SlrPrice;
import com.stackroute.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private Producer producer;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Producer producer) {
        this.productRepository = productRepository;
        this.producer = producer;
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

/**
  Returns product details given product name
 */
    @Override
    public Product getProductDetails(String productName) throws ProductNotExistsException {
            productName = productName.toLowerCase();
            if(productRepository.findByProductName(productName)==null){
                throw new ProductNotExistsException("Product does not exists..");
            }

            Product product = productRepository.findByProductName(productName);
            return product;
    }

    /**
     Save product
     */
    @Override
    public Product saveProduct(Product product) throws ProductAlreadyExistsException {
        product.setProductName(product.getProductName().toLowerCase());
        if(productRepository.findByProductName(product.getProductName())!= null){
            throw new ProductAlreadyExistsException("Product does not exists..");
        }
            Product product1 = productRepository.save(product);
            return product1;
    }
    /**
     Delete product given product name
     */
    @Override
    public boolean deleteProduct(String productName) throws ProductNotExistsException {
        productName = productName.toLowerCase();
        if(productRepository.findByProductName(productName)==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
        Product product = productRepository.findByProductName(productName);
        productRepository.deleteById(product.getProductId());
        return true;
    }
    /**
     Update product given product name
     */
    @Override
    public Product updateProduct(Product product) throws ProductNotExistsException {
        product.setProductName(product.getProductName().toLowerCase());
        if(productRepository.findByProductName(product.getProductName())==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
        return productRepository.save(product);
    }
    /**
     Add seller to a product
     */
    @Override
    public boolean addSeller(String productName, Seller seller) throws ProductNotExistsException {
        productName = productName.toLowerCase();
        if(productRepository.findByProductName(productName)==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
        Product product = getProductDetails(productName);
        List<Seller> sellerList = product.getSellers();
        sellerList.add(seller);
        product.setSellers(sellerList);
        Product product1 = this.updateProduct(product);
        this.producer.sendProduct(product1);
        this.producer.sendProductToMap(product1);
        NewSellerDto newSellerDto = new NewSellerDto();
        newSellerDto.setProductName(product1.getProductName());
        newSellerDto.setSellerEmail(seller.getSellerId());
        newSellerDto.setProductPrice(seller.getProductPrice());
        newSellerDto.setProductStock(seller.getProductStock());
        this.producer.sendNewSeller(newSellerDto);
        ProductsDto productDto=new ProductsDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductImage(product.getProductImage());
        List<SlrPrice> sellersPrice=new ArrayList<>();
        for(Seller sellers:product.getSellers()){
            SlrPrice slrPrice=new SlrPrice();
            slrPrice.setProductPrice(sellers.getProductPrice());
            slrPrice.setSellerEmail(sellers.getSellerId());
            slrPrice.setProductStock(sellers.getProductStock());
            sellersPrice.add(slrPrice);
        }
        productDto.setSellPrice(sellersPrice);
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductBrandName(product.getProductBrandName());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setProductImage(product.getProductImage());
        productDto.setProductSubCategory(product.getProductSubCategory());
        this.producer.sendProductDto(productDto);
        return true;
    }
    /**
     Update seller details
     */
    @Override
    public boolean updateSellerDetails(String productId, Seller seller) throws ProductNotExistsException {
        if(productRepository.findByProductName(productId)==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
        Product product = getProductDetails(productId);
        List<Seller> sellerList= product.getSellers();
        boolean b=false;
        for(Seller s:sellerList) {
            if (s.getSellerId().equals(seller.getSellerId())) {
                b = sellerList.remove(s);
                sellerList.add(seller);
                b = true;
                break;
            }
        }
        product.setSellers(sellerList);
        this.updateProduct(product);
        ProductsDto productDto=new ProductsDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductImage(product.getProductImage());
        List<SlrPrice> sellersPrice=new ArrayList<>();
        for(Seller sellers:product.getSellers()){
            SlrPrice slrPrice=new SlrPrice();
            slrPrice.setProductPrice(sellers.getProductPrice());
            slrPrice.setSellerEmail(sellers.getSellerId());
            slrPrice.setProductStock(sellers.getProductStock());
            sellersPrice.add(slrPrice);
        }
        productDto.setSellPrice(sellersPrice);
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductBrandName(product.getProductBrandName());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setProductImage(product.getProductImage());
        productDto.setProductSubCategory(product.getProductSubCategory());
        this.producer.sendProductDto(productDto);
        return b;
    }
    /**
     Returns Product list for a particular seller
     */
    @Override
    public List<Product> getProductListOfSeller(String sellerId) {

        return null;
    }
    /**
     Returns seller list for a product
     */
    @Override
    public List<Seller> getSellerListOfProduct(String productName) throws ProductNotExistsException {
        productName = productName.toLowerCase();
        if(productRepository.findByProductName(productName)==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
            Product product1 = productRepository.findByProductName(productName);
            List<Seller> sellerList = product1.getSellers();
            return sellerList;

    }
    /**
     Delete seller for a product
     */
    @Override
    public boolean deleteSeller(String productName, String sellerId) throws ProductNotExistsException {
        productName = productName.toLowerCase();
        if(productRepository.findByProductName(productName)==null){
            throw new ProductNotExistsException("Product does not exists..");
        }
        Product product = getProductDetails(productName);
        List<Seller> sellerList= product.getSellers();
        boolean b=false;
        for(Seller s:sellerList){
            if(sellerId.equals(s.getSellerId())){
                b = sellerList.remove(s);
                break;
            }
        }
        product.setSellers(sellerList);
        updateProduct(product);
        return b;
    }

    /**
     delete all product for a seller
     */
    @Override
    public Seller deleteAllProductOfSeller(Seller seller) {
        return null;
    }

    @KafkaListener(topics = "product-Refined", groupId = "product-id",containerFactory = "kafkaListenerContainerFactory")
    public void consumeSeller(@Payload Product product){
        System.out.println(product.toString());
            Product getProduct = productRepository.findByProductName(product.getProductName().toLowerCase());
            getProduct.setProductName(product.getProductName());
            getProduct.setSellers(product.getSellers());
            productRepository.save(getProduct);
    }



}
