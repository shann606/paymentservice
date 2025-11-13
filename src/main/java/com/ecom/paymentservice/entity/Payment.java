package com.ecom.paymentservice.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.ecom.paymentservice.dto.CreatedBy;
import com.ecom.paymentservice.dto.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "online_payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "order_no" ,updatable = false , nullable = false,unique = true)
	private long orderNo;
	@Column(name = "total_amount")
	private BigDecimal amount;
	@Column(name = "created_on" , columnDefinition = "Date")
	private OffsetDateTime createdOn;
	@Column(name = "created_by")
	@Enumerated(EnumType.STRING)
	private CreatedBy createdBy;
	@Column(name="payment_status")
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Column(name="failed_reason")
	private String reason;
	@Column(name="updated_on",columnDefinition = "Date")
	private OffsetDateTime updatedOn;

}
