package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.entity.Role;
import com.example.project_java_web_service_nguyenvandoan.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRoleService {
    Page<UserRole> getUserRoles(Pageable pageable, String roleName);
    Role getRoleDetails(Integer roleId);
    UserRole assignRole(Integer userId, Integer roleId);
    UserRole updateRoleAssignment(Integer userRoleId, Integer newRoleId);
    void revokeRole(Integer userRoleId);
}