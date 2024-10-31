package com.project.gamemarket.service.impl;

import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.service.ProductFindService;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ProductFindServiceImpl implements ProductFindService {

    private final RestClient productClient;
    private final String productServiceEndpoint;

    public ProductFindServiceImpl(@Qualifier("productRestClient") RestClient paymentClient,
                              @Value("${application.product-service.products}") String productServiceEndpoint) {
        this.productClient = paymentClient;
        this.productServiceEndpoint = productServiceEndpoint;
    }

    @Override
    public ProductDetails processFinding(Long id) {
        return productClient.get()
                .uri(productServiceEndpoint+id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    log.error("Server response failed to find this product. Response Code {}", response.getStatusCode());
                    throw new ProductNotFoundException(id);})
                .body(ProductDetails.class);
        }
}
