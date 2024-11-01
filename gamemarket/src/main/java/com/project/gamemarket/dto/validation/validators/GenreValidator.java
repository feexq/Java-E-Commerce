package com.project.gamemarket.dto.validation.validators;

import com.project.gamemarket.common.GenreType;
import com.project.gamemarket.dto.validation.annotation.ValidGenre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class GenreValidator implements ConstraintValidator<ValidGenre, List<String>> {

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        try {
        for (String value : values) {
            GenreType.fromName(value);
        }
        return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}
