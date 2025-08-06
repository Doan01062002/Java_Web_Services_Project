package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<Object> findByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);
}