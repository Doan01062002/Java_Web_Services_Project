package com.example.project_java_web_service_nguyenvandoan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private List<String> roles;
}