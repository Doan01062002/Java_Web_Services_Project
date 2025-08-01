package com.example.project_java_web_service_nguyenvandoan.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OtpVerifyDTO {
    private String username;
    private String password;
    private String otp;
}