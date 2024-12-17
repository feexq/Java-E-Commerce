package com.project.gamemarket.service.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with id %s not found";

    public CustomerNotFoundException(UUID id) { super(String.format(CUSTOMER_NOT_FOUND_MESSAGE, id)); }
}
