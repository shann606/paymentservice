package com.ecom.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.paymentservice.dto.PaymentStatus;
import com.ecom.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

	private PaymentService paymentService;

	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PutMapping("/update")
	ResponseEntity<String> updatePaymentStatus(@RequestParam(name = "orderno", required = true) Long orderNo,
			@RequestParam(name = "status", required = true) PaymentStatus status,
			@RequestParam(name = "reason", required = false) String reason) throws Exception {

		String result;
		int i = paymentService.updatePaymentStatus(orderNo, status, reason);

		if (i == 1)
			result = "Payment Successfully updated";
		else
			result = "Failed to update";

		return ResponseEntity.status(i == 1 ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);

	}

}
