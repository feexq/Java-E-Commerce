package com.project.gamemarket.service.impl;

import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
import com.project.gamemarket.service.KeyActivationService;
import com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class KeyActivationServiceImpl implements KeyActivationService {

    private final RestClient keyClient;
    private final String keyServiceEndpoint;;

    public KeyActivationServiceImpl(@Qualifier("keyRestClient") RestClient keyClient,
                                    @Value("${application.key-service.keys}") String keyServiceEndpoint) {
        this.keyClient = keyClient;
        this.keyServiceEndpoint = keyServiceEndpoint;
    }

    @Override
    public KeyActivationResponseDto processKeyActivation(KeyActivationRequestDto key) {
        return keyClient.post()
                .uri(keyServiceEndpoint)
                .body(key)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    log.error("Server response failed to find this product. Response Code {}", response.getStatusCode());
                    throw new KeyActivationFailedProcessActivation(key.getKey().replace("\"", ""));})
                .body(KeyActivationResponseDto.class);
        }
}
