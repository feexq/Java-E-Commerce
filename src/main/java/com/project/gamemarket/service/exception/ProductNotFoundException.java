package com.project.gamemarket.service.exception;

public class ProductNotFoundException extends RuntimeException {
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with id %s not found";

    public ProductNotFoundException(Object id) { super(String.format(PRODUCT_NOT_FOUND_MESSAGE, id)); }
}
