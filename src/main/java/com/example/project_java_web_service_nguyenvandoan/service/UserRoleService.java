package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRoleService {
    Page<UserRole> getUserRoles(Pageable pageable, String roleName);

    // Lấy thông tin chi tiết phân quyền của một user
    List<UserRole> getUserRolesByUserId(Integer userId);

    // Phân quyền cho user
    UserRole assignRoleToUser(Integer userId, Integer roleId);

    // Cập nhật phân quyền
    UserRole updateUserRole(Integer userRoleId, Integer newRoleId);

    // Thu hồi quyền
    void revokeRoleFromUser(Integer userRoleId);
}