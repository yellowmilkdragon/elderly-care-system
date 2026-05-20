package com.neusoft.elderlycare.controller;

import com.neusoft.elderlycare.common.ApiResponse;
import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.dto.CaregiverAssignRequest;
import com.neusoft.elderlycare.entity.Customer;
import com.neusoft.elderlycare.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final DemoDataStore demoDataStore;

    public UserController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        return ApiResponse.ok(List.copyOf(demoDataStore.getUsers()));
    }

    @GetMapping("/caregivers")
    public ApiResponse<List<Map<String, Object>>> caregivers() {
        List<Map<String, Object>> caregivers = demoDataStore.getUsers().stream()
                .filter(user -> Long.valueOf(2).equals(user.getRoleId()))
                .map(user -> Map.<String, Object>of(
                        "id", user.getId(),
                        "nickname", user.getNickname(),
                        "username", user.getUsername(),
                        "phoneNumber", user.getPhoneNumber(),
                        "customers", demoDataStore.getCustomers().stream()
                                .filter(customer -> user.getId().equals(customer.getUserId()))
                                .map(Customer::getCustomerName)
                                .toList()
                ))
                .toList();
        return ApiResponse.ok(caregivers);
    }

    @PostMapping("/caregivers/assign")
    public ApiResponse<Customer> assignCaregiver(@RequestBody CaregiverAssignRequest request) {
        return ApiResponse.ok("分配成功", demoDataStore.assignCaregiver(request.customerId(), request.caregiverId()));
    }
}
