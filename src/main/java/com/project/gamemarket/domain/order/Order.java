package com.project.gamemarket.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Order {

    String orderId;
    UUID transactionId;
    List<OrderEntry> entries;
    String cartId;
    String customerId;
    Double total;
}
