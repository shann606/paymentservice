package com.ecom.paymentservice.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecom.paymentservice.dto.CreatedBy;
import com.ecom.paymentservice.dto.PaymentStatus;
import com.ecom.paymentservice.entity.Payment;
import com.ecom.paymentservice.events.OrderCreatedEvent;
import com.ecom.paymentservice.events.PaymentResponseEvent;
import com.ecom.paymentservice.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

	private PaymentRepository paymentRepo;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	public PaymentService(PaymentRepository paymentRepo, KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		this.paymentRepo = paymentRepo;

	}

	@Transactional(rollbackOn = Exception.class)
	public void processPayment(OrderCreatedEvent orderEvent) throws Exception {

		Payment payment = new Payment();
		payment.setCreatedBy(CreatedBy.ORDERSERVICE);
		payment.setOrderNo(orderEvent.getOrderNo());
		payment.setAmount(orderEvent.getAmount());
		payment.setCreatedOn(orderEvent.getOrderCreatedOn());
		payment.setPaymentStatus(PaymentStatus.INPROGRESS);
		paymentRepo.saveAndFlush(payment);
		log.info("Payment service completed successfully");
		kafkaTemplate.send("payment-result", PaymentResponseEvent.builder().orderNo(payment.getOrderNo())
				.paymentStatus(payment.getPaymentStatus()).reason(payment.getReason()).build());

	}

	public int updatePaymentStatus(Long orderNo, PaymentStatus status, String reason) throws Exception {

		int i = paymentRepo.updatePaymentStatus(orderNo, status, reason, CreatedBy.PAYMENTSERVICE,
				OffsetDateTime.now());

		if (i == 1) {
			log.info("payment status updated successfully sending back to Order service");
			kafkaTemplate.send("payment-update",
					PaymentResponseEvent.builder().orderNo(orderNo).paymentStatus(status).reason(reason).build());
		}
		return i;
	}

}
