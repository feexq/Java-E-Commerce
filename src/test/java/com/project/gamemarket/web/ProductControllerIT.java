package com.project.gamemarket.web;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamemarket.AbstractIt;
import com.project.gamemarket.domain.ProductDetails;

import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.featuretoggle.FeatureExtension;
import com.project.gamemarket.featuretoggle.FeatureToggles;
import com.project.gamemarket.featuretoggle.anotation.DisableFeature;
import com.project.gamemarket.featuretoggle.anotation.EnableFeature;
import com.project.gamemarket.objects.BuildProducts;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.mapper.ProductMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static com.project.gamemarket.service.exception.CustomerNotFoundException.CUSTOMER_NOT_FOUND_MESSAGE;
import static com.project.gamemarket.service.exception.FeatureNotEnabledException.FEATURE_NOT_ENABLED_MESSAGE;
import static com.project.gamemarket.service.exception.ProductNotFoundException.PRODUCT_NOT_FOUND_MESSAGE;
import static com.project.gamemarket.service.exception.TitleAlreadyExistsException.PRODUCT_TITLE_ALREADY_EXISTS;
import static org.hamcrest.Matchers.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@ExtendWith({FeatureExtension.class, SpringExtension.class})
public class ProductControllerIT extends AbstractIt {

    @SpyBean
    private ProductService productService;

    @Autowired
    private BuildProducts buildProducts;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        reset(productService);
        productRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProduct(){
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(productDetailsDto.getTitle().toLowerCase()))
                .andExpect(jsonPath("$.shortDescription").value(productDetailsDto.getShortDescription()))
                .andExpect(jsonPath("$.price").value(productDetailsDto.getPrice()))
                .andExpect(jsonPath("$.developer").value(productDetailsDto.getDeveloper()))
                .andExpect(jsonPath("$.deviceTypes").isArray())
                .andExpect(jsonPath("$.genres").isArray());

        verify(productService, times(1)).addProduct(ArgumentMatchers.eq(productMapper.toProductDetails(productDetailsDto)));

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldThrowTitleAlreadyExistsCreateProduct(){
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();

        ProductEntity productEntity = productRepository.save(productMapper.toProductEntity(productMapper.toProductDetails(productDetailsDto)));

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(CONFLICT, String.format(PRODUCT_TITLE_ALREADY_EXISTS, productEntity.getTitle()));

        problemDetail.setType(URI.create("title-already-exists"));
        problemDetail.setTitle("Title Already Exists");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto)))
                .andExpect(status().isConflict())
                        .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, times(1)).addProduct(ArgumentMatchers.eq(productMapper.toProductDetails(productDetailsDto)));

    }

    @Test
    @SneakyThrows
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteProductById() {
        long productId = 1L;
        ProductDetails productDetails = productMapper.toProductDetails(productId, buildProducts.buildProductDetailsDtoMock());
        ProductEntity productEntity = productMapper.toProductEntity(productDetails);

        productRepository.save(productEntity);

        mockMvc.perform(delete("/api/v1/products/{id}", productEntity.getId()))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productEntity.getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldGetAllProducts() {
        List<ProductDetails> productDetails = buildProducts.buildProductDetailsListMock();

        List<ProductEntity> productEntities = productDetails.stream().map(productMapper::toProductEntity).toList();

        productRepository.saveAll(productEntities);

        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDetailsEntries", hasSize(buildProducts.buildProductDetailsListMock().size())))
                .andExpect(jsonPath("$.productDetailsEntries[0].title").value("The-Legend-of-Zelda:-Breath-of-the-Wild")) // Sort by price DESC
                .andExpect(jsonPath("$.productDetailsEntries[1].title").value("Cyberpunk-2077")) // Sort by price DESC
                .andExpect(jsonPath("$.productDetailsEntries[*].id").exists())
                .andExpect(jsonPath("$.productDetailsEntries[*].title").exists())
                .andExpect(jsonPath("$.productDetailsEntries[*].price").exists());

        verify(productService, times(1)).getProducts();
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldFindProductById() {
        ProductEntity productEntity = productMapper.toProductEntity(productMapper.toProductDetails(buildProducts.buildProductDetailsDtoMock()));

        productRepository.save(productEntity);

        mockMvc.perform(get("/api/v1/products/{id}", productEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{"
                        + "\"title\": \"witcher-3\","
                        + "\"shortDescription\": \"The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.\","
                        + "\"price\": 30.0,"
                        + "\"developer\": \"CD Projekt Red\","
                        + "\"deviceTypes\": [\"console\", \"pc\"],"
                        + "\"genres\": [\"rpg\", \"mythology\"]"
                        + "}"));
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldThrowNotFoundExceptionFindProductById() {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, 1L));

        problemDetail.setType(URI.create("product-not-found"));
        problemDetail.setTitle("Product Not Found");

        mockMvc.perform(get("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureToggles.SUMMER_SALE)
    @WithMockUser
    void shouldFindProductBySale() {
        List<ProductDetails> productDetails = buildProducts.buildProductDetailsListMock();

        List<ProductEntity> productEntities = productDetails.stream().map(productMapper::toProductEntity).toList();

        productRepository.saveAll(productEntities);

        mockMvc.perform(get("/api/v1/products/sale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDetailsEntries[0].title").value("witcher-3")) // Sort by id in db
                .andExpect(jsonPath("$.productDetailsEntries[1].title").value("god-of-war")); // Sort by id in db
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureToggles.SUMMER_SALE)
    @WithMockUser
    void shouldThrowFeatureNotEnableException() {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(FEATURE_NOT_ENABLED_MESSAGE, FeatureToggles.SUMMER_SALE.getFeatureName()));

        problemDetail.setType(URI.create("feature-disabled"));
        problemDetail.setTitle("Feature is disabled");

        mockMvc.perform(get("/api/v1/products/sale"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduct() {
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();

        ProductEntity productEntity = productRepository.save(productMapper.toProductEntity(productMapper.toProductDetails(productDetailsDto)));

        mockMvc.perform(put("/api/v1/products/{id}", productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto.toBuilder().title("new-title").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new-title"));

        verify(productService, times(1)).updateProduct(ArgumentMatchers.any(ProductDetails.class));

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldDropProductTitleAlreadyExistsUpdateProduct() {
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();
        productRepository.save(productMapper.toProductEntity(productMapper.toProductDetails(buildProducts.buildProductDetailsDto())));

        ProductEntity productEntity = productRepository.save(productMapper.toProductEntity(productMapper.toProductDetails(productDetailsDto)));

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(CONFLICT, String.format(PRODUCT_TITLE_ALREADY_EXISTS, "cyberpunk-2077"));

        problemDetail.setType(URI.create("title-already-exists"));
        problemDetail.setTitle("Title Already Exists");

        mockMvc.perform(put("/api/v1/products/{id}", productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto.toBuilder().title("cyberpunk-2077").build())))
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void shouldDropProductNotFoundUpdateProduct() {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, 1L));

        problemDetail.setType(URI.create("product-not-found"));
        problemDetail.setTitle("Product Not Found");
        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(buildProducts.buildProductDetailsDto())));

    }

    @Test
    @SneakyThrows
    void shouldDropUnauthorizedFakeToken() {
        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer fake-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void shouldDropUnauthorizedWithoutToken() {
        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void shouldDropForbiddenCreateProduct(){
        ProductDetailsDto productDetailsDto = buildProducts.buildProductDetailsDtoMock();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productDetailsDto)))
                .andExpect(status().isForbidden());

    }

}
