package com.project.gamemarket.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Order {

    Long id;
    List<OrderEntry> entries;
    String cartId;
    String payment_reference;
    UUID customerId;
    Double total;
}
