package com.project.gamemarket.service;

import com.project.gamemarket.domain.CustomerDetails;

import java.util.List;

public interface CustomerService {

    List<CustomerDetails> getAllCustomersDetails();

    CustomerDetails getCustomerDetailsById(Long customerId);

}
