package com.project.gamemarket.domain.order;

import com.project.gamemarket.common.GameType;
import com.project.gamemarket.domain.ProductDetails;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrderEntry {

    GameType gameType;
    int quantity;
}
