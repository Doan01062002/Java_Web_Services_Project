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
import java.util.List;

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
    public List<UserRole> getUserRolesByUserId(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return userRoleRepository.findByUserId(userId);
    }

    @Override
    public UserRole assignRoleToUser(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

        // Kiểm tra xem user đã có role này chưa
        boolean roleExists = userRoleRepository.findByUserId(userId)
                .stream()
                .anyMatch(userRole -> userRole.getRoleId().equals(roleId));
        if (roleExists) {
            throw new IllegalStateException("User already has role: " + role.getRoleName());
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole updateUserRole(Integer userRoleId, Integer newRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new EntityNotFoundException("UserRole not found with id: " + userRoleId));
        Role newRole = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + newRoleId));

        // Kiểm tra xem role mới có trùng với role hiện tại không
        if (userRole.getRoleId().equals(newRoleId)) {
            throw new IllegalStateException("User already has role: " + newRole.getRoleName());
        }

        userRole.setRoleId(newRoleId);
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepository.save(userRole);
    }

    @Override
    public void revokeRoleFromUser(Integer userRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new EntityNotFoundException("UserRole not found with id: " + userRoleId));
        userRoleRepository.delete(userRole);
    }
}