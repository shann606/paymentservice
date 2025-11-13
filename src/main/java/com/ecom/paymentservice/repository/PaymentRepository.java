package com.ecom.paymentservice.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecom.paymentservice.dto.CreatedBy;
import com.ecom.paymentservice.dto.PaymentStatus;
import com.ecom.paymentservice.entity.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE Payment p SET p.paymentStatus=:status , p.reason =:reason ,p.createdBy =:createdBy ,p.updatedOn=:updatedOn WHERE p.orderNo=:orderNo")
	int updatePaymentStatus(@Param("orderNo") Long orderNo, @Param("status") PaymentStatus status,
			@Param("reason") String reason, @Param("createdBy") CreatedBy createdBy,
			@Param("updatedOn") OffsetDateTime updatedOn);

}
