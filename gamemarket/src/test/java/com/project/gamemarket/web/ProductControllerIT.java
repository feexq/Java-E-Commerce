package com.project.gamemarket.web;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.domain.ProductDetails;

import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsListDto;
import com.project.gamemarket.objects.BuildProducts;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.mapper.ProductMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@Tag("product-service")
public class ProductControllerIT {

    @MockBean
    private ProductService productService;

    @Autowired
    private BuildProducts buildProducts;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductMapper productMapper;


    @Test
    @SneakyThrows
    void shouldCreateProduct(){
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();

        when(productService.addProduct(any())).thenReturn(productMapper.toProductDetails(productDetailsDto));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(productDetailsDto.getTitle()))
                .andExpect(jsonPath("$.shortDescription").value(productDetailsDto.getShortDescription()))
                .andExpect(jsonPath("$.price").value(productDetailsDto.getPrice()))
                .andExpect(jsonPath("$.developer").value(productDetailsDto.getDeveloper()))
                .andExpect(jsonPath("$.deviceTypes").isArray())
                .andExpect(jsonPath("$.genres").isArray());

        verify(productService, times(1)).addProduct(ArgumentMatchers.any(ProductDetails.class));

    }

    @Test
    @SneakyThrows
    void shouldThrowCustomValidationExceptionCreateProduct() {
        ProductDetailsDto invalidProductDetailsDto = productMapper.toProductDetailsDto(buildProducts.buildThrowCustomValidationExceptionProductDetailsMock());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidProductDetailsDto)))
                .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                        .andExpect(jsonPath("$.title").value("Field Validation Exception"))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.detail").value("Request validation failed"))
                        .andExpect(jsonPath("$.invalidParams", hasSize(greaterThan(0))))
                        .andExpect(jsonPath("$.invalidParams[*].fieldName")
                                .value(containsInAnyOrder("developer")))
                        .andExpect(jsonPath("$.invalidParams[*].reason").exists());
    }

    @Test
    @SneakyThrows
    void shouldThrowValidationExceptionCreateProduct(){
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(buildProducts.buildThrowValidationExceptionProductDetailsMock())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                .andExpect(jsonPath("$.title").value("Field Validation Exception"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.invalidParams", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.invalidParams[*].fieldName")
                        .value(containsInAnyOrder("genres", "title", "price", "developer", "shortDescription", "deviceTypes")))
                .andExpect(jsonPath("$.invalidParams[*].reason").exists());

    }

    @Test
    @SneakyThrows
    void shouldDeleteProductById() {
        long productId = 1L;

        doNothing().when(productService).deleteProduct(productId);
        
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    @SneakyThrows
    void shouldGetAllProducts() {

        when(productService.getProducts()).thenReturn(buildProducts.buildProductDetailsListMock());

        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDetailsEntries", hasSize(buildProducts.buildProductDetailsListMock().size())))
                .andExpect(jsonPath("$.productDetailsEntries[*].id").exists())
                .andExpect(jsonPath("$.productDetailsEntries[*].title").exists())
                .andExpect(jsonPath("$.productDetailsEntries[*].price").exists());

        verify(productService, times(1)).getProducts();
    }

    @Test
    @SneakyThrows
    void shouldFindProductById() {
        long productId = 1L;

        when(productService.getProductById(productId)).thenReturn(buildProducts.buildProductDetailsMock());

        mockMvc.perform(get("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{"
                        + "\"title\": \"Witcher 3\","
                        + "\"shortDescription\": \"The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.\","
                        + "\"price\": 30.0,"
                        + "\"developer\": \"CD Projekt Red\","
                        + "\"deviceTypes\": [\"console\", \"pc\"],"
                        + "\"genres\": [\"rpg\", \"mythology\"]"
                        + "}"));
    }

}
