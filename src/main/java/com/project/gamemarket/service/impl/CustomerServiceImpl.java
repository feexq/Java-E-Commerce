package com.project.gamemarket.service.impl;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.exception.CustomerAlreadyExistsException;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomDetailsMapper customDetailsMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDetails> getAllCustomersDetails() {
        return customDetailsMapper.toCustomerDetailsList(customerRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDetails getCustomerByReference(UUID customerReference) {
        return customerRepository.naturalId(customerReference)
                .map(customDetailsMapper::toCustomerDetails)
                .orElseThrow(() -> new CustomerNotFoundException(customerReference));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerDetails createCustomer(CustomerDetails customerDetails) {
        CustomerEntity customerEntity = customDetailsMapper.toCustomerEntity(customerDetails);
        if (customerRepository.existsByEmail(customerEntity.getEmail()) ||
            customerRepository.existsByPhoneNumber(customerEntity.getPhoneNumber())) {
            throw new CustomerAlreadyExistsException(customerEntity.getPhoneNumber(), customerEntity.getEmail());
        }
        try {
            return customDetailsMapper.toCustomerDetails(customerRepository.save(customerEntity));
        } catch (Exception e) {
            log.error("Error during save new customer");
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(UUID customerReference) {
        try {
            customerRepository.deleteByNaturalId(customerReference);
        } catch (Exception e) {
            log.error("Error during delete customer by id");
            throw new PersistenceException(e);
        }
    }
}
