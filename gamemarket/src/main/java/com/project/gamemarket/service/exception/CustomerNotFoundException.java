package com.project.gamemarket.service.exception;

public class CustomerNotFoundException extends RuntimeException {
    public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with id %s not found";

    public CustomerNotFoundException(Long id) { super(String.format(CUSTOMER_NOT_FOUND_MESSAGE, id)); }
}
