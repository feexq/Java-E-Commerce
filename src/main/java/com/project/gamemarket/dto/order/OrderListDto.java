package com.project.gamemarket.dto.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderListDto {

    List<OrderDto> orders;
}
