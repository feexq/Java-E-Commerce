package com.project.gamemarket.dto.order;

import com.project.gamemarket.domain.order.OrderEntry;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;


@Value
@Builder
public class OrderDto {

    List<OrderEntryDto> entries;
    String cartId;
    String payment_reference;
    UUID customerId;
    Double total;

}
