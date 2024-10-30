package com.project.gamemarket.service.exception;

public class PaymentClientFailedProcessPayment extends RuntimeException{
    private static final String PAYMENT_CLIENT_FAILED_MESSAGE = "Payment Client Failed for cart %s and customer with id %s";

    public PaymentClientFailedProcessPayment(String cartId, String customerId) {
        super(String.format(PAYMENT_CLIENT_FAILED_MESSAGE, cartId, customerId));
    }
}
