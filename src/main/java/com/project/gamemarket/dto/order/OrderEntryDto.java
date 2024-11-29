package com.project.gamemarket.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderEntryDto {

    @NotNull(message = "Game name cannot be null")
    String gameType;

    @NotNull(message = "Quantity cannot be null")
    int quantity;
}
