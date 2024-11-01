package com.project.gamemarket.dto.key;


import com.project.gamemarket.common.KeyActivationStatus;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class KeyActivationResponseDto {

     String key;
     KeyActivationStatus status;
     String productId;

}
