package com.example.project_java_web_service_nguyenvandoan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGrowthStats {
    private String period;
    private Long count;
}