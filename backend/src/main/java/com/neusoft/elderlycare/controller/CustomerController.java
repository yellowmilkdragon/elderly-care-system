package com.neusoft.elderlycare.controller;

import com.neusoft.elderlycare.common.ApiResponse;
import com.neusoft.elderlycare.entity.Customer;
import com.neusoft.elderlycare.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    // 呼杨柯：客户管理模块接口入口，负责入住登记、退住登记等客户档案维护。
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ApiResponse<List<Customer>> list() {
        // 呼杨柯：查询客户列表，供前端客户管理页面统一展示与筛选。
        return ApiResponse.ok(customerService.listCustomers());
    }

    @GetMapping("/{id}")
    public ApiResponse<Customer> detail(@PathVariable Long id) {
        // 呼杨柯：按主键查询客户详情，用于档案详情抽屉展示。
        return ApiResponse.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ApiResponse<Customer> create(@RequestBody Customer customer) {
        // 呼杨柯：新增客户档案，对应入住登记主流程。
        return ApiResponse.ok("鍒涘缓鎴愬姛", customerService.saveCustomer(customer));
    }

    @PutMapping("/{id}")
    public ApiResponse<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        // 呼杨柯：编辑客户档案时，统一以路径参数作为客户主键来源。
        customer.setId(id);
        return ApiResponse.ok("鏇存柊鎴愬姛", customerService.saveCustomer(customer));
    }
}
