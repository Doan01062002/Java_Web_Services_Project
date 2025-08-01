package com.example.project_java_web_service_nguyenvandoan.dto.request;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}