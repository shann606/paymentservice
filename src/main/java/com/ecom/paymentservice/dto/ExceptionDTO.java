package com.ecom.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ExceptionDTO extends RuntimeException {
	private static final long serialVersionUID = 8301394174626095736L;

	private final String status;
	private final String reason;

}
