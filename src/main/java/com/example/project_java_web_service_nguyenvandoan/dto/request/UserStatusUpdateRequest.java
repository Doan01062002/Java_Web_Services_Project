package com.example.project_java_web_service_nguyenvandoan.dto.request;

import com.example.project_java_web_service_nguyenvandoan.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private UserStatus status;
}