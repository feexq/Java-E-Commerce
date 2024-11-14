package com.project.gamemarket.dto.validation.annotation;


import com.project.gamemarket.dto.validation.validators.DeviceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DeviceValidator.class)
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface ValidDevice {

    String DEVICE_NOT_FOUND = "Device type not found in the list of allowed devices. Example of a valid device type: 'PC', 'CONSOLE', 'NintendoSwitch', 'Mobile'.";

    String message() default DEVICE_NOT_FOUND;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
