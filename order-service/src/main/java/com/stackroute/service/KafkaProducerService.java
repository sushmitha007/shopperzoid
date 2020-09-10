package com.stackroute.service;

import com.stackroute.domain.Order;
import com.stackroute.kafka.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerService {

    private static final String TOPIC = "orderDetails";
    private static final String TOPIC_RECOM = "orderDetailsRecom";
    private static final Logger logger= LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, OrderDto> kafkaTemplateRecom;

    public void sendOrderDetails(Order order){
        logger.info(String.format("#### -> Producing Order Details as follows -> %s", order));
        kafkaTemplate.send(TOPIC,order);
    }

    public void sendOrderToRecom(OrderDto orderDto){
        logger.info(String.format("#### -> Producing Order Details as follows to recom-> %s", orderDto));
        kafkaTemplateRecom.send(TOPIC_RECOM,orderDto);
    }


}
