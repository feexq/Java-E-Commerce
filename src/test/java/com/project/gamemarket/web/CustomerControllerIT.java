package com.project.gamemarket.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.AbstractIt;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.objects.BuildCustomers;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.OrderRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import jakarta.persistence.PersistenceException;
import lombok.SneakyThrows;
import lombok.With;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.project.gamemarket.service.exception.CustomerNotFoundException.CUSTOMER_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@DisplayName("Customer Controller IT")
@ExtendWith(SpringExtension.class)
public class CustomerControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BuildCustomers buildCustomers;

    @Autowired
    private CustomDetailsMapper customDetailsMapper;

    @Autowired
    private CustomerController customerController;

    @SpyBean
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        reset(customerService);
        customerRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCustomer() {

        CustomerDetailsDto customerDetailsDto = buildCustomers.buildCustomerDetailsDto();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerDetailsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(customerDetailsDto.getName()))
                .andExpect(jsonPath("$.email").value(customerDetailsDto.getEmail()))
                .andExpect(jsonPath("$.region").value(customerDetailsDto.getRegion()))
                .andExpect(jsonPath("$.phoneNumber").value(customerDetailsDto.getPhoneNumber()))
                .andExpect(jsonPath("$.deviceTypes").isArray());



    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldThrowCustomerAlreadyExistsException() {
        CustomerDetailsDto customerDetailsDto = buildCustomers.buildCustomerDetailsDto();
        customerRepository.save(customDetailsMapper.toCustomerEntity(customDetailsMapper.toCustomerDetails(customerDetailsDto)));

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerDetailsDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("customer-already-exists"))
                .andExpect(jsonPath("$.title").value("Customer Already Exists"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldThrowsValidationExceptionWhitNoValidationCustomerFields() {
        CustomerDetailsDto dto = customDetailsMapper.toCustomerDetailsDto(buildCustomers.buildInvalidCustomerDetails());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                .andExpect(jsonPath("$.title").value("Field Validation Exception"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.invalidParams", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.invalidParams[*].fieldName")
                        .value(containsInAnyOrder("region", "email", "phoneNumber", "name")))
                .andExpect(jsonPath("$.invalidParams[*].reason").exists());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldThrowsCustomValidationException() {
        CustomerDetailsDto dto = customDetailsMapper.toCustomerDetailsDto(buildCustomers.buildCustomInvalidCustomerDetails());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                .andExpect(jsonPath("$.title").value("Field Validation Exception"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.invalidParams", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.invalidParams[*].fieldName")
                        .value(containsInAnyOrder("region")))
                .andExpect(jsonPath("$.invalidParams[*].reason").exists());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldFindByCustomerReference() {
        CustomerDetails customer = buildCustomers.buildCustomerDetails();
        CustomerEntity customerEntity = customDetailsMapper.toCustomerEntity(customer);
        customerRepository.save(customerEntity);

        mockMvc.perform(get("/api/v1/customers/{id}", customerEntity.getCustomerReference())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.region").value(customer.getRegion()))
                .andExpect(jsonPath("$.phoneNumber").value(customer.getPhoneNumber()))
                .andExpect(jsonPath("$.deviceTypes").isArray());

        verify(customerService, times(1)).getCustomerByReference(customerEntity.getCustomerReference());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldThrowsCustomerNotFoundException() {
        UUID customerId = UUID.randomUUID();

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(CUSTOMER_NOT_FOUND_MESSAGE, customerId));

        problemDetail.setType(URI.create("customer-not-found"));
        problemDetail.setTitle("Customer Not Found");

        mockMvc.perform(get("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomerById(customerId);
        });

        verify(customerService, times(2)).getCustomerByReference(customerId);
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldFindAllCustomers() {
        List<CustomerDetails> customerDetails = buildCustomers.buildCustomerDetailsList();

        List<CustomerEntity> customerEntities = customerDetails.stream().map(customDetailsMapper::toCustomerEntity).toList();

        customerRepository.saveAll(customerEntities);

        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDetailsEntries", hasSize(customerDetails.size())))
                .andExpect(jsonPath("$.customerDetailsEntries[0].name").value("Jack Spring"))
                .andExpect(jsonPath("$.customerDetailsEntries[1].name").value("Jack Summer"))
                .andExpect(jsonPath("$.customerDetailsEntries[*].id").exists())
                .andExpect(jsonPath("$.customerDetailsEntries[*].phoneNumber").exists())
                .andExpect(jsonPath("$.customerDetailsEntries[0].email").value("jacksrping@gmail.com"));
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCustomer() {
        CustomerDetails customerDetails = customDetailsMapper.toCustomerDetails(buildCustomers.buildCustomerDetailsDto());

        CustomerEntity customerEntity = customerRepository.save(customDetailsMapper.toCustomerEntity(customerDetails));

        mockMvc.perform(delete("/api/v1/customers/{id}", customerEntity.getCustomerReference()))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(customerEntity.getCustomerReference());
        Assertions.assertEquals(Optional.empty(), customerRepository.naturalId(customerEntity.getCustomerReference()));
    }

}
