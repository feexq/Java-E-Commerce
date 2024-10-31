package com.project.gamemarket.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.Config.MappersTestConfiguration;
import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.objects.BuildCustomers;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.net.URI;
import java.util.List;
import java.util.Random;

import static com.project.gamemarket.service.exception.CustomerNotFoundException.CUSTOMER_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void shouldCreateCustomer() {
        ResponseEntity<CustomerDetailsDto> response = customerController.createCustomer(buildCustomers.buildCustomerDetailsDto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildCustomers.buildCustomerDetailsDto(),response.getBody());
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
    void shouldThrowsCustomValidationException() {
        CustomerDetailsDto dto = customDetailsMapper.toCustomerDetailsDto(buildCustomers.buildInvalidCustomerDetails());

        ConstraintViolationException exception =
                assertThrows(ConstraintViolationException.class,
                        () -> customerController.createCustomer(dto));

        assertTrue(exception.getMessage().contains("The region is not valid. Allowed regions: Ukraine, Poland, Germany, France, USA, Canada, UK, Netherlands, Japan."));

    }

    @Test
    void shouldFindByIdCustomer() {
        when(customerService.getCustomerDetailsById(ID)).thenReturn(buildCustomers.buildCustomerDetails());

        ResponseEntity<CustomerDetailsDto> response = customerController.getCustomerById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildCustomers.buildCustomerDetailsDto(),response.getBody());
        verify(customerService, times(1)).getCustomerDetailsById(ID);
    }

    @Test
    void shouldThrowsCustomerNotFoundException() {
        doThrow(new CustomerNotFoundException(ID))
                .when(customerService).getCustomerDetailsById(ID);

        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomerById(ID);
        });
        verify(customerService, times(1)).getCustomerDetailsById(ID);
    }

}
