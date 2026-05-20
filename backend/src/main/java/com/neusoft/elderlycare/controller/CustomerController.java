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

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ApiResponse<List<Customer>> list() {
        return ApiResponse.ok(customerService.listCustomers());
    }

    @GetMapping("/{id}")
    public ApiResponse<Customer> detail(@PathVariable Long id) {
        return ApiResponse.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ApiResponse<Customer> create(@RequestBody Customer customer) {
        return ApiResponse.ok("创建成功", customerService.saveCustomer(customer));
    }

    @PutMapping("/{id}")
    public ApiResponse<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        return ApiResponse.ok("更新成功", customerService.saveCustomer(customer));
    }
}
