package com.example.project_java_web_service_nguyenvandoan.service.impl;

import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerGroupRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.request.PurchaseRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.response.*;
import com.example.project_java_web_service_nguyenvandoan.entity.Customer;
import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import com.example.project_java_web_service_nguyenvandoan.entity.Purchase;
import com.example.project_java_web_service_nguyenvandoan.entity.User;
import com.example.project_java_web_service_nguyenvandoan.repository.CustomerGroupRepository;
import com.example.project_java_web_service_nguyenvandoan.repository.CustomerRepository;
import com.example.project_java_web_service_nguyenvandoan.repository.PurchaseRepository;
import com.example.project_java_web_service_nguyenvandoan.repository.UserRepository;
import com.example.project_java_web_service_nguyenvandoan.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Page<CustomerGroup> getCustomerGroups(Pageable pageable) {
        return customerGroupRepository.findAll(pageable);
    }

    @Override
    public CustomerGroup createCustomerGroup(CustomerGroupRequest request) {
        CustomerGroup group = new CustomerGroup();
        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());
        return customerGroupRepository.save(group);
    }

    @Override
    public CustomerGroup updateCustomerGroup(Integer groupId, CustomerGroupRequest request) {
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Customer group not found"));
        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());
        group.setUpdatedAt(LocalDateTime.now());
        return customerGroupRepository.save(group);
    }

    @Override
    public void deleteCustomerGroup(Integer groupId) {
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Customer group not found"));
        customerGroupRepository.delete(group);
    }

    @Override
    public Page<Customer> getCustomers(Pageable pageable, String status, Integer groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRoles().stream().noneMatch(role -> role.getRoleName().equals("ADMIN") || role.getRoleName().equals("STAFF"))) {
            Optional<Customer> customerOpt = customerRepository.findByUserId(user.getUserId());
            if (customerOpt.isEmpty()) {
                throw new EntityNotFoundException("Customer not found");
            }
            List<Customer> customerList = Collections.singletonList(customerOpt.get());
            return new PageImpl<>(customerList, pageable, customerList.size());
        }

        Customer.CustomerStatus customerStatus = status != null ? Customer.CustomerStatus.valueOf(status.toUpperCase()) : null;
        if (groupId != null && customerStatus != null) {
            return customerRepository.findByGroupIdAndStatus(groupId, customerStatus, pageable);
        } else if (groupId != null) {
            return customerRepository.findByGroupId(groupId, pageable);
        } else if (customerStatus != null) {
            return customerRepository.findByStatus(customerStatus, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getCustomerDetails(Integer customerId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (user.getRoles().stream().noneMatch(role -> role.getRoleName().equals("ADMIN") || role.getRoleName().equals("STAFF"))
                && !Objects.equals(customer.getUserId(), user.getUserId())) {
            throw new AccessDeniedException("You can only view your own customer details");
        }
        return customer;
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (customerRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new IllegalStateException("Customer already exists for this user");
        }

        Customer customer = new Customer();
        customer.setUserId(request.getUserId());
        customer.setGroupId(request.getGroupId());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setCountry(request.getCountry());
        customer.setStatus(Customer.CustomerStatus.ACTIVE);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer customerId, CustomerRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (user.getRoles().stream().noneMatch(role -> role.getRoleName().equals("ADMIN"))
                && !Objects.equals(customer.getUserId(), user.getUserId())) {
            throw new AccessDeniedException("You can only update your own customer details");
        }

        customer.setGroupId(request.getGroupId());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setCountry(request.getCountry());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomerStatus(Integer customerId, Customer.CustomerStatus status) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customer.setStatus(status);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public void softDeleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customer.setIsDeleted(true);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public Page<Purchase> getCustomerPurchaseHistory(Integer customerId, Pageable pageable, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (user.getRoles().stream().noneMatch(role -> role.getRoleName().equals("ADMIN") || role.getRoleName().equals("STAFF"))
                && !Objects.equals(customer.getUserId(), user.getUserId())) {
            throw new AccessDeniedException("You can only view your own purchase history");
        }

        return purchaseRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public Purchase createPurchase(PurchaseRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (customer.getIsDeleted()) {
            throw new IllegalStateException("Cannot create purchase for deleted customer");
        }

        Purchase purchase = new Purchase();
        purchase.setCustomerId(request.getCustomerId());
        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setCurrency(request.getCurrency());
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setNotes(request.getNotes());
        purchase.setStatus(Purchase.PurchaseStatus.COMPLETED);
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase updatePurchase(Integer purchaseId, PurchaseRequest request) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (customer.getIsDeleted()) {
            throw new IllegalStateException("Cannot update purchase for deleted customer");
        }

        purchase.setCustomerId(request.getCustomerId());
        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setCurrency(request.getCurrency());
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setNotes(request.getNotes());
        purchase.setPurchaseDate(LocalDateTime.now());
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchase(Integer purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
        purchaseRepository.delete(purchase);
    }

    @Override
    public List<CustomerGrowthStats> getCustomerGrowthStats() {
        List<Object[]> rawData = customerRepository.getRawCustomerGrowthStats();
        return rawData.stream()
                .map(row -> new CustomerGrowthStats(
                        (String) row[0], // period
                        ((Number) row[1]).longValue() // count
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerSegment> getCustomerSegments() {
        List<Object[]> rawData = customerRepository.getRawCustomerSegments();
        return rawData.stream()
                .map(row -> new CustomerSegment(
                        (String) row[0],                     // groupName
                        (Long) row[1],                       // count
                        new BigDecimal(row[2].toString())    // totalAmount
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerCLV> getCustomerCLVs() {
        List<Object[]> rawData = customerRepository.getRawCustomerCLVs();
        return rawData.stream()
                .map(row -> new CustomerCLV(
                        (Integer) row[0],                    // customerId
                        (String) row[1],                     // fullName
                        new BigDecimal(row[2].toString())    // totalAmount
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TopCustomer> getTopCustomers(Pageable pageable) {
        Page<Object[]> rawPage = customerRepository.getRawTopCustomers(pageable);
        List<TopCustomer> topCustomers = rawPage.getContent().stream()
                .map(row -> new TopCustomer(
                        (Integer) row[0],                    // customerId
                        (String) row[1],                     // fullName
                        new BigDecimal(row[2].toString())    // totalAmount
                ))
                .collect(Collectors.toList());
        return new PageImpl<>(topCustomers, pageable, rawPage.getTotalElements());
    }

    @Override
    public List<RevenueReport> getRevenueByPeriod() {
        List<Object[]> rawData = purchaseRepository.getRawRevenueByPeriod();
        return rawData.stream()
                .map(row -> new RevenueReport(
                        (String) row[0], // period
                        new BigDecimal(row[1].toString()) // total_revenue
                ))
                .collect(Collectors.toList());
    }
}