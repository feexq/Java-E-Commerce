package com.project.gamemarket.dto.validation.annotation;

import com.project.gamemarket.dto.validation.validators.DeveloperValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DeveloperValidator.class)
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface ValidDeveloperBan {

    String DEVELOPER_SHOULD_NOT_BE_BANNED_MESSAGE = "The developer is on the banned list. You cannot proceed with this project.";

    String message() default DEVELOPER_SHOULD_NOT_BE_BANNED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
