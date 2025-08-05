package com.example.project_java_web_service_nguyenvandoan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCustomer {
    private Integer customerId;
    private String customerName;
    private BigDecimal totalSpent;
}