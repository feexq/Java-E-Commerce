package com.project.gamemarket.dto.validation.validators;

import com.project.gamemarket.dto.validation.annotation.ValidNoSpace;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpaceValidator implements ConstraintValidator<ValidNoSpace, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.contains(" ");
    }
}
