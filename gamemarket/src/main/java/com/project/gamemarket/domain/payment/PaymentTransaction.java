package com.project.gamemarket.domain.payment;

import com.project.gamemarket.common.PaymentStatus;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

import java.util.UUID;

@Value
@Builder
public class PaymentTransaction {

    UUID id;
    PaymentStatus status;
    String cartId;
}
