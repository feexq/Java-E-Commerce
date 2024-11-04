package com.project.gamemarket.dto.key;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class KeyActivationRequestDto {

    String key;
    String customerId;
}
