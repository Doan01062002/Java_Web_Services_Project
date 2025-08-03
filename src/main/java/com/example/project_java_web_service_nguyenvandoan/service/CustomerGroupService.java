package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerGroupRequest;
import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerGroupService {
    Page<CustomerGroup> getCustomerGroups(Pageable pageable, String groupName);
    CustomerGroup createCustomerGroup(CustomerGroupRequest request);
    CustomerGroup updateCustomerGroup(Integer groupId, CustomerGroupRequest request);
    void deleteCustomerGroup(Integer groupId);
}