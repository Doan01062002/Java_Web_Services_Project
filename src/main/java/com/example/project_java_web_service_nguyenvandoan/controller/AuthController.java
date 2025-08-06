package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserLogin;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRegister;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.dto.response.ErrorDetail;
import com.example.project_java_web_service_nguyenvandoan.dto.response.JWTResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@Valid @RequestBody UserRegister userRegister, BindingResult result) {
        if (result.hasErrors()) {
            List<ErrorDetail> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new APIResponse<>(false, "Validation failed", null, errors, null), HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userService.registerUser(userRegister);
            APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
            return new ResponseEntity<>(new APIResponse<>(true, "Register user successfully", data, null, null), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail("credentials", e.getMessage())), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin, BindingResult result) {
        if (result.hasErrors()) {
            List<ErrorDetail> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new APIResponse<>(false, "Validation failed", null, errors, null), HttpStatus.BAD_REQUEST);
        }
        try {
            JWTResponse jwtResponse = userService.login(userLogin);
            APIResponse.DataWrapper<JWTResponse> data = new APIResponse.DataWrapper<>(List.of(jwtResponse), null);
            return new ResponseEntity<>(new APIResponse<>(true, "Login successfully", data, null, null), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, "Invalid username or password", null,
                            Collections.singletonList(new ErrorDetail("credentials", "Invalid username or password")), null),
                    HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail("email", e.getMessage())), null),
                    HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, "Login failed: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail("general", e.getMessage())), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-email/{token}")
    public ResponseEntity<APIResponse<String>> verifyEmail(@PathVariable String token) {
        boolean verified = userService.verifyEmail(token);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of(verified ? "Email verified" : "Invalid token"), null);
        return new ResponseEntity<>(new APIResponse<>(verified, verified ? "Email verified successfully" : "Invalid verification token", data, null, null),
                verified ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}