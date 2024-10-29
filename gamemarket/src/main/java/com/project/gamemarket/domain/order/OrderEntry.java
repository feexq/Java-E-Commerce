package com.project.gamemarket.domain.order;

import com.project.gamemarket.common.GameType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrderEntry {

    GameType gameType;
    int quantity;
}
