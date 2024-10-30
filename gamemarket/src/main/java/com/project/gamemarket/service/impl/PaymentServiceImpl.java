package com.project.gamemarket.service.impl;

import com.project.gamemarket.domain.payment.Payment;
import com.project.gamemarket.domain.payment.PaymentTransaction;
import com.project.gamemarket.dto.payment.PaymentClientRequestDto;
import com.project.gamemarket.dto.payment.PaymentClientResponseDto;
import com.project.gamemarket.service.PaymentService;
import com.project.gamemarket.service.exception.PaymentClientFailedProcessPayment;
import com.project.gamemarket.service.mapper.PaymentServiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RestClient paymentClient;
    private final PaymentServiceMapper paymentServiceMapper;
    private final String paymentServiceEndpoint;

    public PaymentServiceImpl(@Qualifier("paymentRestClient") RestClient paymentClient, PaymentServiceMapper paymentServiceMapper,
                              @Value("${application.payment-service.payments}") String paymentServiceEndpoint) {
        this.paymentClient = paymentClient;
        this.paymentServiceMapper = paymentServiceMapper;
        this.paymentServiceEndpoint = paymentServiceEndpoint;
    }

    public PaymentTransaction processPayment(Payment payment) {
        log.info("Processing payment for cart with id {} and consumer reference: {}", payment.getCartId(), payment.getCustomerId());
        PaymentClientRequestDto paymentClientRequestDto = paymentServiceMapper.toPaymentClientRequestDto(payment);

        PaymentClientResponseDto paymentClientResponseDto = paymentClient.post()
                .uri(paymentServiceEndpoint)
                .body(paymentClientRequestDto)
                .contentType(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    log.error("Server response failed to fetch payment link. Response Code {}", response.getStatusCode());
                    throw new PaymentClientFailedProcessPayment(payment.getCartId(), payment.getCustomerId());
                })
                .body(PaymentClientResponseDto.class);


        return paymentServiceMapper.toPaymentTransaction(payment.getCartId(), paymentClientResponseDto);
    }
}
