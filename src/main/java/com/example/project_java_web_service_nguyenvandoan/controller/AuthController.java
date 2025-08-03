package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserLogin;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRegister;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.dto.response.JWTResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin) {
        JWTResponse jwtResponse = userService.login(userLogin);
        APIResponse.DataWrapper<JWTResponse> data = new APIResponse.DataWrapper<>(List.of(jwtResponse), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Login successfully", data, null, null), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> register(@Valid @RequestBody UserRegister userRegister) {
        User user = userService.registerUser(userRegister);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User registered successfully", data, null, null), HttpStatus.CREATED);
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<APIResponse<String>> verifyEmail(@PathVariable String token) {
        boolean verified = userService.verifyEmail(token);
        String message = verified ? "Email verified successfully" : "Invalid or expired token";
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of(message), null);
        return new ResponseEntity<>(new APIResponse<>(verified, message, data, null, null), HttpStatus.OK);
    }
}