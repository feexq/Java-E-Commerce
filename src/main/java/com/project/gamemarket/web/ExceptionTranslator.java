package com.project.gamemarket.web;

import com.project.gamemarket.service.exception.*;
import com.project.gamemarket.web.exception.ParamsViolationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

import static com.project.gamemarket.util.ValidationDetailsUtils.getValidationErrorsProblemDetail;
import static java.net.URI.create;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.error("customerNotFoundException");
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setStatus(NOT_FOUND);
        problemDetail.setType(create("customer-not-found"));
        problemDetail.setTitle("Customer Not Found");
        return problemDetail;
    }

    @ExceptionHandler(KeyActivationFailedProcessActivation.class)
    ProblemDetail handleKeyActivationFailedProcessActivationException(KeyActivationFailedProcessActivation ex) {
        log.error("KeyActivationFailedProcessActivation");
        ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setStatus(BAD_REQUEST);
        problemDetail.setType(create("key-activation-failed"));
        problemDetail.setTitle("Key Activation Failed");
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFoundException(ProductNotFoundException ex) {
        log.error("ProductNotFoundException");
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setStatus(NOT_FOUND);
        problemDetail.setType(create("product-not-found"));
        problemDetail.setTitle("Product Not Found");
        return problemDetail;
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    ProblemDetail handelCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        log.error("CustomerAlreadyExistsException");
        ProblemDetail problemDetail = forStatusAndDetail(CONFLICT, ex.getMessage());
        problemDetail.setStatus(CONFLICT);
        problemDetail.setType(create("customer-already-exists"));
        problemDetail.setTitle("Customer Already Exists");
        return problemDetail;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex) {
        log.error("OrderNotFoundException");
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setStatus(NOT_FOUND);
        problemDetail.setType(create("order-not-found"));
        problemDetail.setTitle("Order Not Found");
        return problemDetail;
    }

    @ExceptionHandler(TitleAlreadyExistsException.class)
    ProblemDetail handleProductTitleAlreadyExistsException(TitleAlreadyExistsException ex) {
        log.error("TitleAlreadyExistsException");
        ProblemDetail problemDetail = forStatusAndDetail(CONFLICT, ex.getMessage());
        problemDetail.setStatus(BAD_REQUEST);
        problemDetail.setType(create("title-already-exists"));
        problemDetail.setTitle("Title Already Exists");
        return problemDetail;
    }

    @ExceptionHandler(FeatureNotEnabledException.class)
    ProblemDetail handleFeatureToggleNotEnabledException(FeatureNotEnabledException ex) {
        log.info("Feature is not enabled");
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(create("feature-disabled"));
        problemDetail.setTitle("Feature is disabled");
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<ParamsViolationDetails> validationResponse =
                errors.stream().map(err -> ParamsViolationDetails.builder().reason(err.getDefaultMessage()).fieldName(err.getField()).build()).toList();
        log.info("Input params validation failed");
        return ResponseEntity.status(BAD_REQUEST).body(getValidationErrorsProblemDetail(validationResponse));
    }
}
