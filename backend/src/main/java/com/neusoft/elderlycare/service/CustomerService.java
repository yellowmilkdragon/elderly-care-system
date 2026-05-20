package com.neusoft.elderlycare.service;

import com.neusoft.elderlycare.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomerById(Long id);
    Customer saveCustomer(Customer customer);
}
