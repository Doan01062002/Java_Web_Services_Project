package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @EntityGraph(attributePaths = "userRoles")
    Optional<User> findByUsername(String username);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}