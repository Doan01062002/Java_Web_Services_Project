package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRegister;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserUpdateRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserStatusUpdateRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        Page<User> customerPage = userService.getCustomers(PageRequest.of(page, size), username, email);
        APIResponse.Pagination pagination = new APIResponse.Pagination(page, size, customerPage.getTotalPages(), customerPage.getTotalElements());
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(customerPage.getContent(), pagination);
        return new ResponseEntity<>(new APIResponse<>(true, "Customers retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<APIResponse<User>> getCustomerById(@PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.getCustomerById(userId, currentUsername);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> createCustomer(@Valid @RequestBody UserRegister request) {
        User user = userService.createCustomer(request);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer created successfully", data, null, null), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<APIResponse<User>> updateCustomer(
            @PathVariable Integer userId, @Valid @RequestBody UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.updateCustomer(userId, request, currentUsername);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer updated successfully", data, null, null), HttpStatus.OK);
    }

    @PutMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> updateCustomerStatus(
            @PathVariable Integer userId, @Valid @RequestBody UserStatusUpdateRequest request) {
        User user = userService.updateCustomerStatus(userId, request);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer status updated successfully", data, null, null), HttpStatus.OK);
    }
}