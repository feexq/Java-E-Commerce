package com.project.gamemarket.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.project.gamemarket.AbstractTI;
import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.GenreType;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.objects.BuildProducts;
import com.project.gamemarket.service.ProductService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.net.URI;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.project.gamemarket.service.exception.ProductNotFoundException.PRODUCT_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Controller Wiremock IT")
@Tag("product-service-wiremock")
public class ProductControllerWiremockIT extends AbstractTI {

    public static final Long ID = new Random().nextLong();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuildProducts buildProducts;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @SneakyThrows
    void shouldFindProductById() {
        stubFor(WireMock.get("/product-service/v1/" + ID)
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(buildProducts.buildProductDetailsMock()))));

        mockMvc.perform(get("/api/v1/products/wiremock/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.title").value("Witcher 3"))
                .andExpect(jsonPath("$.shortDescription").isNotEmpty())
                .andExpect(jsonPath("$.price").value(30.0))
                .andExpect(jsonPath("$.developer").value("CD Projekt Red"))
                .andExpect(jsonPath("$.deviceTypes").isArray())
                .andExpect(jsonPath("$.deviceTypes[0]").value("console"))
                .andExpect(jsonPath("$.deviceTypes[1]").value("pc"))
                .andExpect(jsonPath("$.genres[0]").value("rpg"))
                .andExpect(jsonPath("$.genres[1]").value("mythology"));
    }

    @Test
    @SneakyThrows
    void shouldDropProductNotFoundException() {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, ID));

        problemDetail.setType(URI.create("product-not-found"));
        problemDetail.setTitle("Product Not Found");

        stubFor(WireMock.get("/product-service/v1/" + ID)
                .willReturn(aResponse().withStatus(404)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(null))));

        mockMvc.perform(get("/api/v1/products/wiremock/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }

}
