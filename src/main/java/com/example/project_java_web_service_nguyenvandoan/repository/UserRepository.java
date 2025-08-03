package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Page<User> findByRolesRoleName(String roleName, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseAndRolesRoleName(String username, String roleName, Pageable pageable);
    Page<User> findByEmailContainingIgnoreCaseAndRolesRoleName(String email, String roleName, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndRolesRoleName(
            String username, String email, String roleName, Pageable pageable);

    boolean existsByUsername(@NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username);
}