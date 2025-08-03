package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRoleAssignmentRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRoleUpdateRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.UserRole;
import com.example.project_java_web_service_nguyenvandoan.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserRole>> getUserRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roleName) {
        Page<UserRole> userRolePage = userRoleService.getUserRoles(PageRequest.of(page, size), roleName);
        APIResponse.Pagination pagination = new APIResponse.Pagination(page, size, userRolePage.getTotalPages(), userRolePage.getTotalElements());
        APIResponse.DataWrapper<UserRole> data = new APIResponse.DataWrapper<>(userRolePage.getContent(), pagination);
        return new ResponseEntity<>(new APIResponse<>(true, "User roles retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserRole>> getUserRolesByUserId(@PathVariable Integer userId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userId);
        APIResponse.DataWrapper<UserRole> data = new APIResponse.DataWrapper<>(userRoles, null);
        return new ResponseEntity<>(new APIResponse<>(true, "User roles for user retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserRole>> assignRoleToUser(@RequestBody UserRoleAssignmentRequest request) {
        UserRole userRole = userRoleService.assignRoleToUser(request.getUserId(), request.getRoleId());
        APIResponse.DataWrapper<UserRole> data = new APIResponse.DataWrapper<>(List.of(userRole), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Role assigned successfully", data, null, null), HttpStatus.CREATED);
    }

    @PutMapping("/{userRoleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserRole>> updateUserRole(@PathVariable Integer userRoleId, @RequestBody UserRoleUpdateRequest request) {
        UserRole userRole = userRoleService.updateUserRole(userRoleId, request.getNewRoleId());
        APIResponse.DataWrapper<UserRole> data = new APIResponse.DataWrapper<>(List.of(userRole), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User role updated successfully", data, null, null), HttpStatus.OK);
    }

    @DeleteMapping("/{userRoleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> revokeRoleFromUser(@PathVariable Integer userRoleId) {
        userRoleService.revokeRoleFromUser(userRoleId);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Role revoked"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Role revoked successfully", data, null, null), HttpStatus.OK);
    }
}