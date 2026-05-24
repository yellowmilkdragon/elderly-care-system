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

    // 刘宸宇：用户管理接口；张文辉：健康管家分配与服务对象设置接口。
    private final DemoDataStore demoDataStore;

    public UserController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        // 刘宸宇：查询系统用户基础信息，支撑用户管理页面。
        return ApiResponse.ok(List.copyOf(demoDataStore.getUsers()));
    }

    @GetMapping("/caregivers")
    public ApiResponse<List<Map<String, Object>>> caregivers() {
        // 张文辉：聚合健康管家及其服务对象，为分配页面提供展示数据。
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
        // 张文辉：提交健康管家分配结果，更新客户的服务归属。
        return ApiResponse.ok("鍒嗛厤鎴愬姛", demoDataStore.assignCaregiver(request.customerId(), request.caregiverId()));
    }
}
