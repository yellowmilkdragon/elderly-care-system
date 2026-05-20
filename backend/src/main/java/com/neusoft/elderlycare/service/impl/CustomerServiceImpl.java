package com.neusoft.elderlycare.service.impl;

import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.entity.Customer;
import com.neusoft.elderlycare.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final DemoDataStore demoDataStore;

    public CustomerServiceImpl(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @Override
    public List<Customer> listCustomers() {
        return demoDataStore.getCustomers();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return demoDataStore.getCustomers().stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return demoDataStore.saveCustomer(customer);
    }
}
