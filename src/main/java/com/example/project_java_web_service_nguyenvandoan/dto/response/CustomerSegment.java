package com.example.project_java_web_service_nguyenvandoan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSegment {
    private String segment;
    private Long count;
    private BigDecimal totalSpent;
}