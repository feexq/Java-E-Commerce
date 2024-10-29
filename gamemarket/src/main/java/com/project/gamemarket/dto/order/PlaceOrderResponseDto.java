package com.project.gamemarket.dto.order;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
public class PlaceOrderResponseDto {

    String orderId;
    UUID transactionId;
}
