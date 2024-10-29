package com.project.gamemarket.service.impl;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final List<CustomerDetails> customers = buildCustomerDetailsMock();

    @Override
    public List<CustomerDetails> getAllCustomersDetails() {
        return customers;
    }

    @Override
    public CustomerDetails getCustomerDetailsById(Long customerId) {
        return Optional.of(customers.stream()
                        .filter(details -> details.getId().equals(customerId)).findFirst())
                .get()
                .orElseThrow(() -> {log.info("Customer with id " + customerId + " not found");
                return new CustomerNotFoundException(customerId); });
    }

    private List<CustomerDetails> buildCustomerDetailsMock() {
        return List.of(
                CustomerDetails.builder()
                        .id(1L)
                        .name("Jack Spring")
                        .email("jacksrping@gmail.com")
                        .phoneNumber("+1234567890")
                        .region("Ukraine")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .build(),
                CustomerDetails.builder()
                        .id(2L)
                        .name("Emma Johnson")
                        .email("emma.johnson@example.com")
                        .phoneNumber("+1234567891")
                        .region("USA")
                        .deviceTypes(List.of(DeviceType.PC, DeviceType.MOBILE))
                        .build(),
                CustomerDetails.builder()
                        .id(3L)
                        .name("Liam Smith")
                        .email("liam.smith@example.com")
                        .phoneNumber("+1234567892")
                        .region("UK")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.MOBILE))
                        .build(),
                CustomerDetails.builder()
                        .id(4L)
                        .name("Olivia Brown")
                        .email("olivia.brown@example.com")
                        .phoneNumber("+1234567893")
                        .region("Canada")
                        .deviceTypes(List.of(DeviceType.PC, DeviceType.CONSOLE))
                        .build(),
                CustomerDetails.builder()
                        .id(5L)
                        .name("Noah Wilson")
                        .email("noah.wilson@example.com")
                        .phoneNumber("+1234567894")
                        .region("Australia")
                        .deviceTypes(List.of(DeviceType.MOBILE))
                        .build());
    }
}
