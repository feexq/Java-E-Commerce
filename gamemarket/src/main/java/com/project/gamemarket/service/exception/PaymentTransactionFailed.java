package com.project.gamemarket.service.exception;

import java.util.UUID;

public class PaymentTransactionFailed extends RuntimeException{
    private static final String PAYMENT_TRANSACTION_FAILED = "Payment transaction failed with id: %s for cart with id %s";

    public PaymentTransactionFailed(UUID id, String cartId) {
        super(String.format(PAYMENT_TRANSACTION_FAILED, id, cartId));
    }
}
