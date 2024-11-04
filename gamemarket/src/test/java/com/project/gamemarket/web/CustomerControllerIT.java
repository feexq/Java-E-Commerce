package com.project.gamemarket.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.objects.BuildCustomers;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static com.project.gamemarket.service.exception.CustomerNotFoundException.CUSTOMER_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Customer Controller IT")
@Tag("customer-service")
public class CustomerControllerIT {

    public static final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private BuildCustomers buildCustomers;

    @Autowired
    private CustomDetailsMapper customDetailsMapper;


    @Test
    @SneakyThrows
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
    void shouldFindByIdCustomer() {
        CustomerDetails customer = buildCustomers.buildCustomerDetails();

        when(customerService.getCustomerDetailsById(ID)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.region").value(customer.getRegion()))
                .andExpect(jsonPath("$.phoneNumber").value(customer.getPhoneNumber()))
                .andExpect(jsonPath("$.deviceTypes").isArray());

        verify(customerService, times(1)).getCustomerDetailsById(ID);
    }

    @Test
    @SneakyThrows
    void shouldThrowsCustomerNotFoundException() {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(CUSTOMER_NOT_FOUND_MESSAGE, ID));

        problemDetail.setType(URI.create("customer-not-found"));
        problemDetail.setTitle("Customer Not Found");

        doThrow(new CustomerNotFoundException(ID))
                .when(customerService).getCustomerDetailsById(ID);

        mockMvc.perform(get("/api/v1/customers/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomerById(ID);
        });

        verify(customerService, times(2)).getCustomerDetailsById(ID);




    }

}
