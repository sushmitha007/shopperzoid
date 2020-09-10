package com.stackroute;

import com.stackroute.domain.Products;
import com.stackroute.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.function.DoubleUnaryOperator;

@Component
public class SendEmail {
    @Autowired
    private JavaMailSender javaMailSender;

    private static Logger logger = LoggerFactory.getLogger(SendEmail.class);

    @KafkaListener(topics = "orderEmail", groupId = "order-id",containerFactory = "kafkaListenerContainerFactory")
    public void consumeSeller(@Payload Order order)  {

        logger.info("Inside order detail",order);

        this.sendEmail(order);
    }
    public void sendEmail(Order order) {
        String pattern = "dd-MMM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(order.getTimestamp());
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(order.getBuyerEmail());

        msg.setSubject("Shopperzoid.stackroute.io");
        String sendMsg="";
        Double initialPrice=0.0;
        Double finalPrice=0.0;
        for(Products products:order.getProducts()){
            initialPrice+=products.getProductPrice();
            finalPrice+=products.getProductFinalPrice();
        }
        Double difference=initialPrice-finalPrice;
        if(difference==0.0){
            sendMsg="Sorry! You are unable to save money on this order. "+"\n"+"Better shopping next time "+"\n"+"Enjoy Shopping at ShopperZoid";
        }
        else{
            sendMsg="Congratuations! You saved Rs. "+ difference+" money on this order. "+"\n"+"Enjoy Shopping at ShopperZoid";
        }



        msg.setText("Hi Customer,\n" +
                "Your order has been placed successfully.\n" +
                "Your order details are:\n" + "Order Id :" + order.getOrderId() + "\n"+
                "Order placed on "+ date +"\n" +
                   sendMsg +"\n" +
                "Thank you for shopping from Shopperzoid\n" +
                "Team Shopper-zoid");

        javaMailSender.send(msg);

    }
}
