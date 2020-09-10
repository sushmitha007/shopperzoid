package com.stackroute.service;

import com.stackroute.domain.Product;
import com.stackroute.exceptions.DatabaseConnectivityFailedException;
import com.stackroute.exceptions.SellerNotFoundException;
import com.stackroute.kafka.ProductDto;
import com.stackroute.kafka.ProductsDto;
import com.stackroute.kafka.SellerDto;
import com.stackroute.kafka.SlrPrice;
import com.stackroute.repository.SellerRepository;
import com.stackroute.domain.Seller;
import com.stackroute.exceptions.SellerAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService{

    private SellerRepository sellerRepository;

    /**
     * Constructor based Dependency injection to inject SellerRepository here
     */
    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Implementation of saveSeller method
     */
    @Override
    public Seller saveSeller(Seller seller) throws SellerAlreadyExistsException {
        /**Throw SellerAlreadyExistsException if track already exists*/
        if(sellerRepository.existsById(seller.getSellerEmail())){
            throw new SellerAlreadyExistsException();
        }

        Seller savedSeller = sellerRepository.save(seller);

        return savedSeller;
    }
    /**
     * Implementation of getSellerById method
     */

    @Override
    public Seller getSellerById(String sellerEmail) throws SellerNotFoundException {
        /**Throw SellerNotFoundException if Seller we want to get is not found*/
        if(!sellerRepository.existsById(sellerEmail)){
            throw new SellerNotFoundException();
        }
        Seller retrievedSellerById = sellerRepository.findById(sellerEmail).get();
        return retrievedSellerById;

    }

    /*Implementation of getAllSeller method*/

    @Override
    public List<Seller> getAllSeller() throws DatabaseConnectivityFailedException {
        /**Throws DatabaseConnectivityFailedException if Database connection issue happens*/

        return sellerRepository.findAll();
    }

    /*implementation of deleteSellerById method*/

    @Override
    public Seller deleteSellerById(String sellerEmail) throws SellerNotFoundException {
        /* Throws SellerNotFoundException if seller to be deleted is not found*/
        if(!sellerRepository.existsById(sellerEmail)){
            throw new SellerNotFoundException();
        }
        Seller deletedSeller = sellerRepository.findById(sellerEmail).get();
        sellerRepository.deleteById(sellerEmail);
        return deletedSeller;
    }

    /*implementation of deleteAllSeller method*/

    @Override
    public boolean deleteAllSeller() throws DatabaseConnectivityFailedException {
        /*throws DatabaseConnectivityFailedException if database is not connected*/
        if(sellerRepository.findAll().isEmpty()){
            return false;
        }
        sellerRepository.deleteAll();
        return true;
    }

    /*implementation of updateSeller method*/

    @Override
    public Seller updateSeller(Seller seller) throws SellerNotFoundException {
        /*throws SellerNotFoundException if seller to be updated is not found*/
        if(!sellerRepository.existsById(seller.getSellerEmail())){
            throw new SellerNotFoundException();
        }
        sellerRepository.save(seller);
        Seller updatedSeller = sellerRepository.findById(seller.getSellerEmail()).get();

        return updatedSeller;
    }

    /*implementation of getSellerByName method*/

    @Override
    public List<Seller> getSellerByName(String sellerName) throws SellerNotFoundException {
        /*throws SellerNotFoundException exception if seller is not found*/
        if(sellerRepository.findBySellerName(sellerName).isEmpty()){
            throw new SellerNotFoundException();
        }
        return sellerRepository.findBySellerName(sellerName);
    }

    @KafkaListener(topics = "product-Info-2", groupId = "product-id", containerFactory = "kafkaListenerContainerFactory")
    public void productDto(@Payload ProductsDto productDto) {
        System.out.println(productDto.toString());
        for (SlrPrice slrPrice : productDto.getSellPrice()) {
            Seller seller = sellerRepository.findById(slrPrice.getSellerEmail()).get();
            boolean productAvailable = false;
            for (Product products : seller.getSellerProducts()) {
                if (products.getProductName().equalsIgnoreCase(productDto.getProductName())) {
                    products.setProductName(productDto.getProductName());
                    products.setProductBrandName(productDto.getProductBrandName());
                    products.setProductCategory(productDto.getProductCategory());
                    products.setProductDescription(productDto.getProductDescription());
                    products.setProductImage(productDto.getProductImage());
                    products.setProductSubCategory(productDto.getProductSubCategory());
                    products.setProductStock(slrPrice.getProductStock());
                    products.setProductPrice(slrPrice.getProductPrice());
                    sellerRepository.save(seller);
                    productAvailable = true;
                }
            }
            if (productAvailable == false) {
                Product product1 = new Product();
                product1.setProductName(productDto.getProductName());
                product1.setProductBrandName(productDto.getProductBrandName());
                product1.setProductCategory(productDto.getProductCategory());
                product1.setProductDescription(productDto.getProductDescription());
                product1.setProductImage(productDto.getProductImage());
                product1.setProductSubCategory(productDto.getProductSubCategory());
                product1.setProductStock(slrPrice.getProductStock());
                product1.setProductPrice(slrPrice.getProductPrice());
                seller.addProduct(product1);
                sellerRepository.save(seller);
            }

        }
    }

    @KafkaListener(topics = "product-dto", groupId = "product-id", containerFactory = "kafkaListenerContainerFactory")
    public void productUpdate(@Payload ProductDto productDto){
        Seller seller=sellerRepository.findById(productDto.getSellerEmail()).get();
        for(Product product:seller.getSellerProducts()){
            if(product.getProductName().equals(productDto.getProductName())){
                product.setProductStock(productDto.getProductStock());
            }
        }
        sellerRepository.save(seller);
    }

    @KafkaListener(topics = "product-dto", groupId = "product-id", containerFactory = "kafkaListenerContainerFactory")
    public void productupdate(@Payload ProductDto productDto){
        Seller seller=sellerRepository.findById(productDto.getSellerEmail()).get();
        for(Product product:seller.getSellerProducts()){
            if(product.getProductName().equals(productDto.getProductName())){
                product.setProductStock(productDto.getProductStock());
            }
        }
        sellerRepository.save(seller);

    }
}
