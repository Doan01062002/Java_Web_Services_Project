package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> getUserById(Integer id);
    User updateUserInfo(Integer id, User user);
    void toggleUserActive(Integer id);
    void softDeleteUser(Integer id);
}
