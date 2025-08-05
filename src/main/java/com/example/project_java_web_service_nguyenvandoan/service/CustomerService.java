package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerGroupRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.PurchaseRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.response.*;
import com.example.project_java_web_service_nguyenvandoan.entity.Customer;
import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import com.example.project_java_web_service_nguyenvandoan.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<CustomerGroup> getCustomerGroups(Pageable pageable);
    CustomerGroup createCustomerGroup(CustomerGroupRequest request);
    CustomerGroup updateCustomerGroup(Integer groupId, CustomerGroupRequest request);
    void deleteCustomerGroup(Integer groupId);
    Page<Customer> getCustomers(Pageable pageable, String status, Integer groupId, String username);
    Customer getCustomerDetails(Integer customerId, String username);
    Customer createCustomer(CustomerRequest request);
    Customer updateCustomer(Integer customerId, CustomerRequest request, String username);
    void updateCustomerStatus(Integer customerId, Customer.CustomerStatus status);
    void softDeleteCustomer(Integer customerId);
    Page<Purchase> getCustomerPurchaseHistory(Integer customerId, Pageable pageable, String username);
    Purchase createPurchase(PurchaseRequest request);
    Purchase updatePurchase(Integer purchaseId, PurchaseRequest request);
    void deletePurchase(Integer purchaseId);
    List<CustomerGrowthStats> getCustomerGrowthStats();
    List<CustomerSegment> getCustomerSegments();
    List<CustomerCLV> getCustomerCLVs();
    Page<TopCustomer> getTopCustomers(Pageable pageable);
    List<RevenueReport> getRevenueByPeriod();
}