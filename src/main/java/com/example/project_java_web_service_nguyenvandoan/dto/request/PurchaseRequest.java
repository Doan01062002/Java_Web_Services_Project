package com.example.project_java_web_service_nguyenvandoan.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;

    @NotNull(message = "Currency is required")
    private String currency;

    private String paymentMethod;
    private String notes;
}