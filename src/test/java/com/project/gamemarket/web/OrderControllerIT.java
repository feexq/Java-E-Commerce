package com.project.gamemarket.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.AbstractIt;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.dto.order.OrderEntryDto;
import com.project.gamemarket.dto.order.OrderRequestDto;
import com.project.gamemarket.featuretoggle.FeatureToggles;
import com.project.gamemarket.objects.BuildCustomers;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.OrderRepository;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.service.CustomerService;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.mapper.CustomDetailsMapper;
import com.project.gamemarket.service.mapper.OrderMapper;
import lombok.SneakyThrows;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.project.gamemarket.service.exception.CustomerNotFoundException.CUSTOMER_NOT_FOUND_MESSAGE;
import static com.project.gamemarket.service.exception.FeatureNotEnabledException.FEATURE_NOT_ENABLED_MESSAGE;
import static com.project.gamemarket.service.exception.OrderNotFoundException.ORDER_NOT_FOUND_EXCEPTION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@DisplayName("Order Controller IT")
@ExtendWith(SpringExtension.class)
public class OrderControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private OrderRepository orderRepository;

    @SpyBean
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        reset(orderService);
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldCreateOrder() {
        CustomerEntity customerEntity = createCustomer();
        createProduct();
        OrderRequestDto orderRequestDto = buildOrderRequestDto();

        mockMvc.perform(post("/api/v1/orders/{customerReference}/{cartId}", customerEntity.getCustomerReference(), "cart-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerEntity.getCustomerReference().toString()))
                .andExpect(jsonPath("$.cartId").value("cart-123"))
                .andExpect(jsonPath("$.entries[0].gameType").value("cyberpunk-2077"));
    }

    @Test
    @SneakyThrows
    void shouldThrowProductNotFoundException() {
        CustomerEntity customerEntity = createCustomer();
        OrderRequestDto orderRequestDto = buildOrderRequestDto();

        mockMvc.perform(post("/api/v1/orders/{customerReference}/{cartId}", customerEntity.getCustomerReference(), "cart-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Product Not Found"));
    }

    @Test
    @SneakyThrows
    void shouldThrowCustomerNotFoundException() {
        OrderRequestDto orderRequestDto = buildOrderRequestDto();

        mockMvc.perform(post("/api/v1/orders/{customerReference}/{cartId}", UUID.randomUUID(), "cart-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Customer Not Found"));
    }

    @Test
    @SneakyThrows
    void shouldThrowPersistenceException() {
        CustomerEntity customerEntity = createCustomer();
        createProduct();
        OrderRequestDto orderRequestDto = buildInvalidOrderRequestDto();

        mockMvc.perform(post("/api/v1/orders/{customerReference}/{cartId}", customerEntity.getCustomerReference(), "cart-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderRequestDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Persistence Exception"));
    }

    @Test
    @SneakyThrows
    void shouldGetOrderByCartId(){
        saveOrder();

        mockMvc.perform(get("/api/v1/orders/{cartId}", "cart-123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-123"))
                .andExpect(jsonPath("$.entries[0].gameType").value("cyberpunk-2077"));

    }

    @Test
    @SneakyThrows
    void shouldThrowOrderNotFoundException(){
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(ORDER_NOT_FOUND_EXCEPTION, "cart-123"));

        problemDetail.setType(URI.create("order-not-found"));
        problemDetail.setTitle("Order Not Found");

        mockMvc.perform(get("/api/v1/orders/{cartId}", "cart-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }

    @Test
    @SneakyThrows
    void shouldFindAllOrders() {
        saveOrder();

        mockMvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].cartId").value("cart-123"))
                .andExpect(jsonPath("$.orders[0].customerId").value("dd594131-2180-407d-a181-4078086e03ca"))
                .andExpect(jsonPath("$.orders[0].entries").isEmpty()); // OrderSummaryProjection
    }

    @Test
    @SneakyThrows
    void shouldDeleteOrder() {
        Order order = saveOrder();

        mockMvc.perform(delete("/api/v1/orders/{id}", order.getCartId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(Optional.empty(), orderRepository.naturalId(order.getCartId()));

    }

    private CustomerEntity createCustomer() {
        return customerRepository.save(CustomerEntity.builder()
                .customerReference(UUID.fromString("dd594131-2180-407d-a181-4078086e03ca"))
                .id(1L)
                .email("test@gmail.com")
                .name("test")
                .phoneNumber("1234567890")
                .build());
    }

    private ProductEntity createProduct() {
        return productRepository.save(ProductEntity.builder()
                .id(1L)
                .price(111D)
                .title("cyberpunk-2077")
                .developer("CD Projekt Red")
                .shortDescription("Projekt Red")
                .build());
    }

    private OrderRequestDto buildOrderRequestDto() {
        return OrderRequestDto.builder()
                .entries(List.of(
                        OrderEntryDto.builder().gameType("cyberpunk-2077").quantity(1).build()
                )).total(111D).build();
    }
    private OrderRequestDto buildInvalidOrderRequestDto() {
        return OrderRequestDto.builder()
                .entries(List.of(
                        OrderEntryDto.builder().gameType("cyberpunk-2077").quantity(1).build(),
                        OrderEntryDto.builder().gameType("cyberpunk-2077").quantity(1).build()
                )).total(111D).build();
    }

    private Order saveOrder() {
        CustomerEntity customerEntity = createCustomer();
        createProduct();
        OrderRequestDto orderRequestDto = buildOrderRequestDto();
        return orderService.placeOrder(orderMapper.toOrder("cart-123",customerEntity.getCustomerReference().toString(),orderRequestDto));
    }

}
