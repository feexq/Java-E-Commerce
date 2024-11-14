package com.project.gamemarket.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderContext {

    String cartId;
    String customerId;
    List<OrderEntry> orderEntries;
    Double totalPrice;
}
