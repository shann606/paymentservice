package com.ecom.paymentservice.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecom.paymentservice.events.OrderCreatedEvent;
import com.ecom.paymentservice.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderCreatedListener {
	
	private PaymentService paymentService;
	
	@Autowired
	public OrderCreatedListener(PaymentService paymentService) {
		this.paymentService=paymentService;
	}
	
	
	@KafkaListener(topics = "order-created" , groupId = "payment-service-group")
	public void orderConsume(OrderCreatedEvent orderEvent) throws Exception {
		log.info("receving the topic for the order no ::"+ orderEvent.getOrderNo());
		paymentService.processPayment(orderEvent);
		
		
	}

}
