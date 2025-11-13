package com.ecom.paymentservice.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentDTO implements Serializable {
	private static final long serialVersionUID = -6933318350310400045L;

	private long id;

	private long orderNo;

	private OffsetDateTime createdOn;

	private CreatedBy createdBy;

	private PaymentStatus paymentStatus;

	private String reason;

	private OffsetDateTime updatedOn;

}
