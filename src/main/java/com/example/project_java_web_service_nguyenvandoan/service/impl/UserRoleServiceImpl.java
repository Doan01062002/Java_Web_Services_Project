package com.example.project_java_web_service_nguyenvandoan.service.impl;

import com.example.project_java_web_service_nguyenvandoan.entity.Role;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.entity.UserRole;
import com.example.project_java_web_service_nguyenvandoan.repository.RoleRepository;
import com.example.project_java_web_service_nguyenvandoan.repository.UserRepository;
import com.example.project_java_web_service_nguyenvandoan.repository.UserRoleRepository;
import com.example.project_java_web_service_nguyenvandoan.service.UserRoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<UserRole> getUserRoles(Pageable pageable, String roleName) {
        if (roleName != null && !roleName.isEmpty()) {
            return userRoleRepository.findByRoleRoleNameContainingIgnoreCase(roleName, pageable);
        }
        return userRoleRepository.findAll(pageable);
    }

    @Override
    public Role getRoleDetails(Integer roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    public UserRole assignRole(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole updateRoleAssignment(Integer userRoleId, Integer newRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new EntityNotFoundException("User role not found"));
        Role role = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        userRole.setRoleId(newRoleId);
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepository.save(userRole);
    }

    @Override
    public void revokeRole(Integer userRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new EntityNotFoundException("User role not found"));
        userRoleRepository.delete(userRole);
    }
}