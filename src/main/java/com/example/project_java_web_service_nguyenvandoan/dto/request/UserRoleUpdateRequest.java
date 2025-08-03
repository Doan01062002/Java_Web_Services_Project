package com.example.project_java_web_service_nguyenvandoan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateRequest {
    private Integer newRoleId;
}