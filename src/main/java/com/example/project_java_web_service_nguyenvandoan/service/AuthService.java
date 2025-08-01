package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserLogin;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRegister;
import com.example.project_java_web_service_nguyenvandoan.dto.response.JWTResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.User;

public interface AuthService {
    User registerUser(UserRegister userRegister);
    JWTResponse login(UserLogin userLogin);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
