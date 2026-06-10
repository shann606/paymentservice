package com.ecom.paymentservice.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.util.ReflectionTestUtils;

import com.ecom.paymentservice.dto.CreatedBy;
import com.ecom.paymentservice.dto.PaymentStatus;
import com.ecom.paymentservice.entity.Payment;
import com.ecom.paymentservice.events.OrderCreatedEvent;
import com.ecom.paymentservice.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class) 
@EmbeddedKafka(topics = "test", partitions = 1)
class PaymentServiceTest {
	@Mock
	private PaymentRepository paymentRepo;
    @Mock
	private KafkaTemplate<String, Object> kafkaTemplate;

	private CompletableFuture<SendResult<String, Object>> future;

	@InjectMocks
	private PaymentService paymentService;

	private static Payment payment;
	private static OrderCreatedEvent orderEvent;

	private static Clock fixedClock = Clock.fixed(Instant.parse("2026-05-14T10:15:30Z"), ZoneOffset.UTC);

	private static OffsetDateTime currentDate = OffsetDateTime.now(fixedClock);

	@BeforeAll
	static void testInit() {
		payment = new Payment();
		payment.setCreatedBy(CreatedBy.ORDERSERVICE);
		payment.setOrderNo(0001);
		payment.setAmount(new BigDecimal(1200));
		payment.setCreatedOn(currentDate);
		payment.setPaymentStatus(PaymentStatus.INPROGRESS);

		orderEvent = new OrderCreatedEvent(0001, currentDate, new BigDecimal(1200));

	}

	@SuppressWarnings("unchecked")
	@BeforeEach
	void testKafkaCall() {

		future = CompletableFuture.completedFuture(mock(SendResult.class));
		when(kafkaTemplate.send(any(String.class), any())).thenReturn(future);

		ReflectionTestUtils.setField(paymentService, "result", "result");
		ReflectionTestUtils.setField(paymentService, "update", "update");

	}

	@Test
	void testProcessPayment() {

		when(paymentRepo.saveAndFlush(any(Payment.class))).thenReturn(payment);

		paymentService.processPayment(orderEvent);
		assertTrue(true);

		verify(paymentRepo, times(1)).saveAndFlush(any(Payment.class));

	}

	@Test
	void testUpdatePaymentStatus() throws Exception {

		doReturn(1).when(paymentRepo).updatePaymentStatus(eq(1L), eq(PaymentStatus.CONFIRMED), eq("test"),
				eq(CreatedBy.PAYMENTSERVICE), any(OffsetDateTime.class));
		paymentService.updatePaymentStatus(0001l, PaymentStatus.CONFIRMED, "test");
		assertTrue(true);

		verify(paymentRepo).updatePaymentStatus(eq(1L), eq(PaymentStatus.CONFIRMED), eq("test"),
				eq(CreatedBy.PAYMENTSERVICE), any(OffsetDateTime.class));

	}

}
