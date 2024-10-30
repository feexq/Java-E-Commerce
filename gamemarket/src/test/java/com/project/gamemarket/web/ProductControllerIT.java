package com.project.gamemarket.web;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.project.gamemarket.AbstractTI;
import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.GenreType;
import com.project.gamemarket.domain.ProductDetails;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.gamemarket.service.exception.ProductNotFoundException.PRODUCT_NOT_FOUND_MESSAGE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@Tag("product-service")
public class ProductControllerIT extends AbstractTI {

    public static final String ID = "ID";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"1"})
    @SneakyThrows
    void shouldFindProductById(String id) {
        stubFor(WireMock.get("/product-service/v1/" + id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(ProductDetails.builder()
                                .id(Long.parseLong(id))
                                .title("Witcher 3")
                                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                                .price(30.0)
                                .developer("CD Projekt Red")
                                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                                .genres(List.of(GenreType.RPG, GenreType.MYTHOLOGY))
                                .build()))));

        mockMvc.perform(get("/api/v1/products/wiremock/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Witcher 3"))
                .andExpect(jsonPath("$.shortDescription").isNotEmpty())
                .andExpect(jsonPath("$.price").value(30.0))
                .andExpect(jsonPath("$.developer").value("CD Projekt Red"))
                .andExpect(jsonPath("$.deviceTypes").isArray());
//                .andExpect(jsonPath("$.deviceTypes").value(List.of(DeviceType.CONSOLE, DeviceType.PC)))
//                .andExpect(jsonPath("$.genres").value(List.of(GenreType.RPG, GenreType.MYTHOLOGY)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1"})
    @SneakyThrows
    void shouldDropFindProductById(String id) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, id));

        problemDetail.setType(URI.create("product-not-found"));
        problemDetail.setTitle("Product Not Found");

        stubFor(WireMock.get("/product-service/v1/" + id)
                .willReturn(aResponse().withStatus(404)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(ProductDetails.builder()
                                .id(Long.parseLong(id))
                                .title("Witcher 3")
                                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                                .price(30.0)
                                .developer("CD Projekt Red")
                                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                                .genres(List.of(GenreType.RPG, GenreType.MYTHOLOGY))
                                .build()))));

        mockMvc.perform(get("/api/v1/products/wiremock/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));
    }


}
