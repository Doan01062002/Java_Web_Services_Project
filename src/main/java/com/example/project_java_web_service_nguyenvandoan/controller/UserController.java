package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserUpdate;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.dto.response.ErrorDetail;
import com.example.project_java_web_service_nguyenvandoan.entity.Session;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<APIResponse<User>> getProfile(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIResponse<>(false, "Không được xác thực", null,
                                Collections.singletonList(new ErrorDetail("Vui lòng cung cấp token hợp lệ")), null));
            }
            User user = userService.getUserByUsername(authentication.getName());
            APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
            return new ResponseEntity<>(new APIResponse<>(true, "Profile retrieved successfully", data, null, null), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new APIResponse<>(false, "Không có quyền truy cập: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(false, "Lỗi khi lấy profile: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<APIResponse<User>> updateProfile(@RequestBody UserUpdate userUpdate, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIResponse<>(false, "Không được xác thực", null,
                                Collections.singletonList(new ErrorDetail("Vui lòng cung cấp token hợp lệ")), null));
            }
            User user = userService.updateProfile(authentication.getName(), userUpdate);
            APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
            return new ResponseEntity<>(new APIResponse<>(true, "Profile updated successfully", data, null, null), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new APIResponse<>(false, "Không có quyền truy cập: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(false, "Lỗi khi cập nhật profile: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        }
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<APIResponse<String>> changePassword(@RequestBody String newPassword, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIResponse<>(false, "Không được xác thực", null,
                                Collections.singletonList(new ErrorDetail("Vui lòng cung cấp token hợp lệ")), null));
            }
            userService.changePassword(authentication.getName(), newPassword);
            APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Password changed"), null);
            return new ResponseEntity<>(new APIResponse<>(true, "Password changed successfully", data, null, null), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new APIResponse<>(false, "Không có quyền truy cập: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(false, "Lỗi khi đổi mật khẩu: " + e.getMessage(), null,
                            Collections.singletonList(new ErrorDetail(e.getMessage())), null));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
        APIResponse.Pagination pagination = new APIResponse.Pagination(page, size, userPage.getTotalPages(), userPage.getTotalElements());
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(userPage.getContent(), pagination);
        return new ResponseEntity<>(new APIResponse<>(true, "Users retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdate) {
        User user = userService.updateUser(id, userUpdate);
        APIResponse.DataWrapper<User> data = new APIResponse.DataWrapper<>(List.of(user), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User updated successfully", data, null, null), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> updateUserStatus(@PathVariable Integer id, @RequestBody Boolean isActive) {
        userService.updateUserStatus(id, isActive);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Status updated"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User status updated successfully", data, null, null), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("User deleted"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "User deleted successfully", data, null, null), HttpStatus.OK);
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Session>> getActiveSessions() {
        List<Session> sessions = userService.getActiveSessions();
        APIResponse.DataWrapper<Session> data = new APIResponse.DataWrapper<>(sessions, null);
        return new ResponseEntity<>(new APIResponse<>(true, "Active sessions retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> deleteSession(@PathVariable String sessionId) {
        userService.deleteSession(sessionId);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Session deleted"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Session deleted successfully", data, null, null), HttpStatus.OK);
    }

    @PostMapping("/sessions/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> cleanupExpiredSessions() {
        userService.cleanupExpiredSessions();
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Expired sessions cleaned up"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Expired sessions cleaned up successfully", data, null, null), HttpStatus.OK);
    }
}