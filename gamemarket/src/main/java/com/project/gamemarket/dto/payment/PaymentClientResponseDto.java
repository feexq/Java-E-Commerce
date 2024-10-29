package com.project.gamemarket.dto.payment;

import com.project.gamemarket.common.PaymentStatus;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class PaymentClientResponseDto {

    UUID uuid;
    PaymentStatus status;
    String customerId;
}
