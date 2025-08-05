package com.example.project_java_web_service_nguyenvandoan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReport {
    private String period;
    private BigDecimal totalRevenue;
}