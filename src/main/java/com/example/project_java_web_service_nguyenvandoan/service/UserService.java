package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.dto.request.UserLogin;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserRegister;
import com.example.project_java_web_service_nguyenvandoan.dto.request.UserUpdate;
import com.example.project_java_web_service_nguyenvandoan.dto.response.JWTResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User registerUser(UserRegister userRegister);
    JWTResponse login(UserLogin userLogin);
    boolean verifyEmail(String token);
    User getUserByUsername(String username);
    User updateProfile(String username, UserUpdate userUpdate);
    void changePassword(String username, String newPassword);
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(Integer id);
    User updateUser(Integer id, UserUpdate userUpdate);
    void updateUserStatus(Integer id, Boolean isActive);
    void deleteUser(Integer id);
    void sendVerificationEmail(User user);
}