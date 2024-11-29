package com.project.gamemarket.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.project.gamemarket.AbstractIt;
import com.project.gamemarket.common.KeyActivationStatus;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.project.gamemarket.service.exception.FeatureNotEnabledException.FEATURE_NOT_ENABLED_MESSAGE;
import static com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation.KEY_S_ACTIVATION_FAILED_PROCESS_ACTIVATION;
import static java.net.URI.create;
import static org.mockito.Mockito.reset;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@AutoConfigureMockMvc
@DisplayName("Product Controller Wiremock IT")
@Tag("product-service-wiremock")
@ExtendWith(FeatureExtension.class)
public class ProductControllerKeyActivationIT extends AbstractIt {

    private static final String CUSTOMER_REFERENCE = "Petrik";
    private static final String KEY = "B2Q65-R9DN8-674S7";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BuildProducts buildProducts;

    @BeforeEach
    void setUp() {
        reset(productService);
        productRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureToggles.KEY_ACTIVATION)
    void shouldFindProductByActivationKey() {
        KeyActivationRequestDto keyActivationRequestDto = KeyActivationRequestDto.builder().customerId(CUSTOMER_REFERENCE).key("B2Q65-R9DN8-674S7").build();

        ProductEntity productEntity = productRepository.save(productMapper.toProductEntity(productMapper.toProductDetails(buildProducts.buildProductDetailsDtoMock())));

        stubFor(WireMock.post("/key-service/v1/activate")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(KeyActivationResponseDto.builder().status(KeyActivationStatus.SUCCESS).productId(productEntity.getId().toString()).key("B2Q65-R9DN8-674S7").build()))));

        mockMvc.perform(post("/api/v1/products/{customerReference}/activate",keyActivationRequestDto.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(keyActivationRequestDto.getKey())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(objectMapper.writeValueAsString(buildProducts.buildProductDetailsDtoMock())));

    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureToggles.KEY_ACTIVATION)
    void shouldDropProductNotFoundException() {
        KeyActivationRequestDto keyActivationRequestDto = KeyActivationRequestDto.builder().customerId(CUSTOMER_REFERENCE).key("B2Q65-R9DN8-674S7").build();

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(BAD_REQUEST, String.format(KEY_S_ACTIVATION_FAILED_PROCESS_ACTIVATION,keyActivationRequestDto.getKey()));

        problemDetail.setType(URI.create("key-activation-failed"));
        problemDetail.setTitle("Key Activation Failed");

        stubFor(WireMock.post("/key-service/v1/activate")
                .willReturn(aResponse().withStatus(400)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(KeyActivationResponseDto.builder()
                                .status(KeyActivationStatus.EXPIRED)
                                .productId("1")
                                .key("B2Q65-R9DN8-674S7")
                                .build()))));

        mockMvc.perform(post("/api/v1/products/{customerReference}/activate",CUSTOMER_REFERENCE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(KEY)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureToggles.KEY_ACTIVATION)
    void shouldDropFeatureNotEnableException() {
        KeyActivationRequestDto keyActivationRequestDto = KeyActivationRequestDto.builder().customerId(CUSTOMER_REFERENCE).key("B2Q65-R9DN8-674S7").build();

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(FEATURE_NOT_ENABLED_MESSAGE, FeatureToggles.KEY_ACTIVATION.getFeatureName()));

        problemDetail.setType(URI.create("feature-disabled"));
        problemDetail.setTitle("Feature is disabled");

        mockMvc.perform(post("/api/v1/products/{customerReference}/activate",keyActivationRequestDto.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(keyActivationRequestDto.getKey())))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }


}
