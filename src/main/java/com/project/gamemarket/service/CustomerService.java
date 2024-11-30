package com.project.gamemarket.service;

import com.project.gamemarket.domain.CustomerDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDetails> getAllCustomersDetails();

    CustomerDetails getCustomerByReference(UUID customerReference);

    CustomerDetails createCustomer(CustomerDetails customerDetails);

    void deleteCustomer(UUID customerReference);

}
