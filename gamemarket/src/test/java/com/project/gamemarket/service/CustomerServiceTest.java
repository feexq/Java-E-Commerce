package com.project.gamemarket.service;

import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.impl.CustomerServiceImpl;
import com.project.gamemarket.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = {CustomerServiceImpl.class})
@DisplayName("Product Service Tests")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void shouldFindProductById() {
        CustomerDetails customerDetails = customerService.getAllCustomersDetails().get(0);
        CustomerDetails customerFound = customerService.getCustomerDetailsById(customerDetails.getId());

        assertEquals(customerDetails.getId(), customerFound.getId());
        assertEquals(customerDetails.getName(), customerFound.getName());
        assertEquals(customerDetails, customerFound);
    }

    @Test
    void shouldThrowProductNotFoundException() {
        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerDetailsById(new Random().nextLong()));
    }
}
