package com.project.gamemarket.service.exception;

public class TitleAlreadyExistsException extends RuntimeException {

    public static final String PRODUCT_TITLE_ALREADY_EXISTS = "Product with title %s already exists";

    public TitleAlreadyExistsException(String title) {super(String.format(PRODUCT_TITLE_ALREADY_EXISTS, title));}
}
