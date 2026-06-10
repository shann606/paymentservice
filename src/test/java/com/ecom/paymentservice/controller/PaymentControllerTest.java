package com.ecom.paymentservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecom.paymentservice.dto.PaymentStatus;
import com.ecom.paymentservice.service.PaymentService;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

	@MockitoBean
	private PaymentService paymentService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testUpdatePaymentStatus() throws Exception {

		when(paymentService.updatePaymentStatus(any(Long.class), any(PaymentStatus.class), any(String.class))).thenReturn(1);

		mockMvc.perform(put("/api/v1/payments/update").param("orderno", "100").param("status", "CONFIRMED")
				.param("reason", "test")).andExpect(status().isAccepted());

		verify(paymentService, times(1)).updatePaymentStatus(any(Long.class), any(PaymentStatus.class), any(String.class));

	}

}
