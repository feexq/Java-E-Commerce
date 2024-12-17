package com.project.gamemarket.dto.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;
@Value
@Builder
public class OrderRequestDto {

    List<OrderEntryDto> entries;
    Double total;

}
