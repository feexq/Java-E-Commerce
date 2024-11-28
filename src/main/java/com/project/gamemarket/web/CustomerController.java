package com.project.gamemarket.web;

import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.dto.customer.CustomerDetailsListDto;

import com.project.gamemarket.dto.validation.ExtendedValidation;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated(ExtendedValidation.class)
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomDetailsMapper customDetailsMapper;

    public CustomerController(CustomerService customerService, CustomDetailsMapper customDetailsMapper) {
        this.customerService = customerService;
        this.customDetailsMapper = customDetailsMapper;
    }

    @GetMapping
    public ResponseEntity<CustomerDetailsListDto> getAllCustomers() {
        return ResponseEntity.ok(customDetailsMapper.toCustomerDetailsListDto(customerService.getAllCustomersDetails()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDto> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(customDetailsMapper.toCustomerDetailsDto(customerService.getCustomerByReference(id)));
    }

    @PostMapping
    public ResponseEntity<CustomerDetailsDto> createCustomer(@RequestBody @Valid CustomerDetailsDto customerDetailsDto){
        return ResponseEntity.ok(customDetailsMapper.toCustomerDetailsDto(customerService.createCustomer(customDetailsMapper.toCustomerDetails(customerDetailsDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDetailsDto> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
