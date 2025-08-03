package com.example.project_java_web_service_nguyenvandoan.service.impl;

import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerGroupRequest;
import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import com.example.project_java_web_service_nguyenvandoan.repository.CustomerGroupRepository;
import com.example.project_java_web_service_nguyenvandoan.service.CustomerGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerGroupServiceImpl implements CustomerGroupService {

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Override
    public Page<CustomerGroup> getCustomerGroups(Pageable pageable, String groupName) {
        if (groupName != null && !groupName.isEmpty()) {
            return customerGroupRepository.findByGroupNameContainingIgnoreCase(groupName, pageable);
        }
        return customerGroupRepository.findAll(pageable);
    }

    @Override
    public CustomerGroup createCustomerGroup(CustomerGroupRequest request) {
        if (customerGroupRepository.existsByGroupName(request.getGroupName())) {
            throw new IllegalStateException("Group name already exists: " + request.getGroupName());
        }

        CustomerGroup group = CustomerGroup.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return customerGroupRepository.save(group);
    }

    @Override
    public CustomerGroup updateCustomerGroup(Integer groupId, CustomerGroupRequest request) {
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Customer group not found with id: " + groupId));

        if (!group.getGroupName().equals(request.getGroupName()) &&
                customerGroupRepository.existsByGroupName(request.getGroupName())) {
            throw new IllegalStateException("Group name already exists: " + request.getGroupName());
        }

        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());
        group.setUpdatedAt(LocalDateTime.now());
        return customerGroupRepository.save(group);
    }

    @Override
    public void deleteCustomerGroup(Integer groupId) {
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Customer group not found with id: " + groupId));
        customerGroupRepository.delete(group);
    }
}