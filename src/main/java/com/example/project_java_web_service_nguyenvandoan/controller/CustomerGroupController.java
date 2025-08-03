package com.example.project_java_web_service_nguyenvandoan.controller;

import com.example.project_java_web_service_nguyenvandoan.dto.request.CustomerGroupRequest;
import com.example.project_java_web_service_nguyenvandoan.dto.response.APIResponse;
import com.example.project_java_web_service_nguyenvandoan.entity.CustomerGroup;
import com.example.project_java_web_service_nguyenvandoan.service.CustomerGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-groups")
public class CustomerGroupController {

    @Autowired
    private CustomerGroupService customerGroupService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CustomerGroup>> getCustomerGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String groupName) {
        Page<CustomerGroup> groupPage = customerGroupService.getCustomerGroups(PageRequest.of(page, size), groupName);
        APIResponse.Pagination pagination = new APIResponse.Pagination(page, size, groupPage.getTotalPages(), groupPage.getTotalElements());
        APIResponse.DataWrapper<CustomerGroup> data = new APIResponse.DataWrapper<>(groupPage.getContent(), pagination);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer groups retrieved successfully", data, null, null), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CustomerGroup>> createCustomerGroup(@Valid @RequestBody CustomerGroupRequest request) {
        CustomerGroup group = customerGroupService.createCustomerGroup(request);
        APIResponse.DataWrapper<CustomerGroup> data = new APIResponse.DataWrapper<>(List.of(group), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer group created successfully", data, null, null), HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CustomerGroup>> updateCustomerGroup(
            @PathVariable Integer groupId, @Valid @RequestBody CustomerGroupRequest request) {
        CustomerGroup group = customerGroupService.updateCustomerGroup(groupId, request);
        APIResponse.DataWrapper<CustomerGroup> data = new APIResponse.DataWrapper<>(List.of(group), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer group updated successfully", data, null, null), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> deleteCustomerGroup(@PathVariable Integer groupId) {
        customerGroupService.deleteCustomerGroup(groupId);
        APIResponse.DataWrapper<String> data = new APIResponse.DataWrapper<>(List.of("Customer group deleted"), null);
        return new ResponseEntity<>(new APIResponse<>(true, "Customer group deleted successfully", data, null, null), HttpStatus.OK);
    }
}