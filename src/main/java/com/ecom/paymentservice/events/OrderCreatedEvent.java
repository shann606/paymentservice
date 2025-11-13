package com.ecom.paymentservice.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderCreatedEvent {

	private long orderNo;
	private OffsetDateTime orderCreatedOn;
	private BigDecimal amount;

}
