package com.project.gamemarket.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PlaceOrderRequestDto {

    @NotNull(message = "Entries is required")
    List<OrderEntryDto> orderEntries;

    @NotNull(message = "Total price is required")
    @Min(value = 0)
    Double totalPrice;
}
