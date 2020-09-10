package com.stackroute.service;


import com.stackroute.domain.Book;
import com.stackroute.domain.Product;
import com.stackroute.kafka.BookRecomDto;
import com.stackroute.kafka.NewSellerDto;
import com.stackroute.kafka.ProductRecomDto;
import com.stackroute.kafka.ProductsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final Logger logger= LoggerFactory.getLogger(Producer.class);

    private static final String TOPIC_PRODUCT="product-Info";

    private static final String TOPIC_PRODUCT_DTO="product-Info-2";

    private static final String TOPIC_PRODUCT_1="product-Info-1";

    private static final String TOPIC_BOOK="book-Info";

    private static final String TOPIC_PRODUCT_RECOM="product-recomm-Info";

    private static final String TOPIC_BOOK_RECOM="book-recom-Info";

    private static final String TOPIC_NEW_SELLER="product-new-seller";

    @Autowired
    private KafkaTemplate<String, Product> kafkaTemplateProduct;

    @Autowired
    private KafkaTemplate<String, Book> kafkaTemplateBook;

    @Autowired
    private KafkaTemplate<String,ProductRecomDto> kafkaTemplateProductRecom;

    @Autowired
    private KafkaTemplate<String,BookRecomDto> kafkaTemplateBookRecom;

    @Autowired
    private KafkaTemplate<String,NewSellerDto> kafkaTemplateNewSeller;

    @Autowired
    private KafkaTemplate<String, ProductsDto> kafkaTemplateProductDto;

    public void sendProduct( Product product) {
        logger.info(String.format("#### -> Producing Products as follows -> %s", product));
        kafkaTemplateProduct.send(TOPIC_PRODUCT, product);
    }

    public void sendProductDto( ProductsDto productDto) {
        logger.info(String.format("#### -> Producing ProductsDto as follows -> %s", productDto));
        kafkaTemplateProductDto.send(TOPIC_PRODUCT_DTO, productDto);
    }

    public void sendProductToMap( Product product) {
        logger.info(String.format("#### -> Producing Products to map as follows -> %s", product));
        kafkaTemplateProduct.send(TOPIC_PRODUCT_1, product);
    }

    public void sendBook( Book book) {
        kafkaTemplateBook.send(TOPIC_BOOK, book);
    }

    public void sendProductRecom(ProductRecomDto productRecomDto) {
        logger.info(String.format("#### -> Producing Product Recom as follows -> %s", productRecomDto));
        kafkaTemplateProductRecom.send(TOPIC_PRODUCT_RECOM,productRecomDto);}

    public void sendNewSeller(NewSellerDto newSellerDto) {kafkaTemplateNewSeller.send(TOPIC_NEW_SELLER,newSellerDto);}

    public void sendBookRecom(BookRecomDto bookRecomDto) {kafkaTemplateBookRecom.send(TOPIC_BOOK_RECOM,bookRecomDto);}



}
