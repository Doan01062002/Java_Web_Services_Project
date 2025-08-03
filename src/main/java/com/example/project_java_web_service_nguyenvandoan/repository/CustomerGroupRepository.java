package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Integer> {
    Page<CustomerGroup> findByGroupNameContainingIgnoreCase(String groupName, Pageable pageable);

    boolean existsByGroupName(@NotBlank(message = "Group name is required") @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters") String groupName);
}