package com.project.gamemarket.service.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public static final String ORDER_NOT_FOUND_EXCEPTION = "Customer with cart id %s not found";

    public OrderNotFoundException(String cartId) { super(String.format(ORDER_NOT_FOUND_EXCEPTION, cartId)); }
}
