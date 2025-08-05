package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.dto.response.CustomerCLV;
import com.example.project_java_web_service_nguyenvandoan.dto.response.CustomerGrowthStats;
import com.example.project_java_web_service_nguyenvandoan.dto.response.CustomerSegment;
import com.example.project_java_web_service_nguyenvandoan.dto.response.TopCustomer;
import com.example.project_java_web_service_nguyenvandoan.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserId(Integer userId);
    Page<Customer> findByGroupId(Integer groupId, Pageable pageable);
    Page<Customer> findByStatus(Customer.CustomerStatus status, Pageable pageable);
    Page<Customer> findByGroupIdAndStatus(Integer groupId, Customer.CustomerStatus status, Pageable pageable);
    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false")
    Page<Customer> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.userId = :userId AND c.isDeleted = false")
    Optional<Customer> findActiveByUserId(Integer userId);

    @Query(value = "SELECT DATE_FORMAT(c.created_at, '%Y-%m') AS period, COUNT(c.customer_id) AS count " +
            "FROM customers c WHERE c.is_deleted = false " +
            "GROUP BY DATE_FORMAT(c.created_at, '%Y-%m')",
            nativeQuery = true)
    List<Object[]> getRawCustomerGrowthStats();

    @Query("SELECT cg.groupName, COUNT(c), COALESCE(SUM(p.totalAmount), 0.0) " +
            "FROM Customer c LEFT JOIN CustomerGroup cg ON c.groupId = cg.groupId " +
            "LEFT JOIN Purchase p ON c.customerId = p.customerId " +
            "WHERE c.isDeleted = false GROUP BY cg.groupName")
    List<Object[]> getRawCustomerSegments();

    @Query("SELECT c.customerId, u.fullName, COALESCE(SUM(p.totalAmount), 0.0) " +
            "FROM Customer c JOIN User u ON c.userId = u.userId " +
            "LEFT JOIN Purchase p ON c.customerId = p.customerId " +
            "WHERE c.isDeleted = false GROUP BY c.customerId, u.fullName")
    List<Object[]> getRawCustomerCLVs();

    @Query("SELECT c.customerId, u.fullName, COALESCE(SUM(p.totalAmount), 0.0) " +
            "FROM Customer c JOIN User u ON c.userId = u.userId " +
            "LEFT JOIN Purchase p ON c.customerId = p.customerId " +
            "WHERE c.isDeleted = false GROUP BY c.customerId, u.fullName " +
            "ORDER BY COALESCE(SUM(p.totalAmount), 0.0) DESC")
    Page<Object[]> getRawTopCustomers(Pageable pageable);
}