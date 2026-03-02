package com.ecom.paymentservice.listener;

import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.ecom.paymentservice.events.OrderCreatedEvent;
import com.ecom.paymentservice.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderCreatedListener {

	private PaymentService paymentService;

	@Lazy
	public OrderCreatedListener(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@RetryableTopic(attempts = "4",  backoff = @Backoff(delay = 1000), autoCreateTopics = "true", dltTopicSuffix = "-dlt")
	@KafkaListener(topics = "${service.topic.order.created}", groupId = "payment-service-group")
	public void orderConsume(OrderCreatedEvent orderEvent, Acknowledgment ack) {
		log.info("receving the topic for the order no ::" + orderEvent.getOrderNo());
		paymentService.processPayment(orderEvent);

		ack.acknowledge();

	}

}
