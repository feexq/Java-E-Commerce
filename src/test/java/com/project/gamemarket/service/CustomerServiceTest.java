package com.project.gamemarket.service;


import com.project.gamemarket.GameMarketApplication;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.service.exception.CustomerAlreadyExistsException;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.impl.CustomerServiceImpl;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CustomerServiceImpl.class})
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomDetailsMapper customDetailsMapper;

    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("Delete Customer - Persistence Exception")
    void testDeleteCustomer_PersistenceException() {
        Long customerId = 1L;
        doThrow(new RuntimeException("Delete failed")).when(customerRepository).deleteById(customerId);

        assertThrows(PersistenceException.class, () ->
                customerService.deleteCustomer(customerId)
        );
    }
}