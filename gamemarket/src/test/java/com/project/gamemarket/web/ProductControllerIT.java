package com.project.gamemarket.web;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.domain.ProductDetails;

import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsListDto;
import com.project.gamemarket.objects.BuildProducts;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.mapper.ProductMapper;
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
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private ProductController productController;

    @Autowired
    private ProductMapper productMapper;


    @Test
    void shouldCreateProduct(){
        ProductDetails productDetails = productMapper.toProductDetails(buildProducts.buildProductDetailsDtoMock());

        when(productService.addProduct(ArgumentMatchers.any(ProductDetails.class))).thenReturn(productDetails);

        ResponseEntity<ProductDetailsDto> response = productController.createProduct(buildProducts.buildProductDetailsDtoMock());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildProducts.buildProductDetailsDtoMock(),response.getBody());
        verify(productService, times(1)).addProduct(ArgumentMatchers.any(ProductDetails.class));

    }

    @Test
    void shouldThrowCustomValidationExceptionCreateProduct(){

        ConstraintViolationException exception =
                assertThrows(ConstraintViolationException.class,
                        () -> productController.createProduct(productMapper.toProductDetailsDto(buildProducts.buildThrowCustomValidationExceptionProductDetailsMock())));

        assertTrue(exception.getMessage().contains("The developer is on the banned list. You cannot proceed with this project."));

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
    void shouldDeleteProductById() {

        ResponseEntity<ProductDetailsDto> response = productController.deleteProductById(anyLong());
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(anyLong());
    }

    @Test
    void shouldGetAllProducts() {

        when(productService.getProducts()).thenReturn(buildProducts.buildProductDetailsListMock());

        ResponseEntity<ProductDetailsListDto> response = productController.getProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productMapper.toProductDetailsListDto(buildProducts.buildProductDetailsListMock()),response.getBody());
        verify(productService, times(1)).getProducts();
    }


}
