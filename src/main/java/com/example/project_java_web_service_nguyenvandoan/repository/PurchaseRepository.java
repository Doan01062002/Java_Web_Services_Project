package com.example.project_java_web_service_nguyenvandoan.repository;

import com.example.project_java_web_service_nguyenvandoan.dto.response.RevenueReport;
import com.example.project_java_web_service_nguyenvandoan.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Page<Purchase> findByCustomerId(Integer customerId, Pageable pageable);

    @Query(value = "SELECT DATE_FORMAT(p.purchase_date, '%Y-%m') AS period, SUM(p.total_amount) AS total_revenue " +
            "FROM purchases p WHERE p.status = 'COMPLETED' " +
            "GROUP BY DATE_FORMAT(p.purchase_date, '%Y-%m')",
            nativeQuery = true)
    List<Object[]> getRawRevenueByPeriod();
}