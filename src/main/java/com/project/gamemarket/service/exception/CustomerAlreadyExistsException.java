package com.project.gamemarket.service.exception;

public class CustomerAlreadyExistsException extends RuntimeException{

    public static final String CUSTOMER_ALREADY_EXISTS = "Customer with phone number %s, email %s, already exists";

    public CustomerAlreadyExistsException(String phoneNumber, String email) {super(String.format(CUSTOMER_ALREADY_EXISTS, phoneNumber, email));}
}
