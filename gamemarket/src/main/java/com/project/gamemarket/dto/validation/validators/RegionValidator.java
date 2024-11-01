package com.project.gamemarket.dto.validation.validators;

import com.project.gamemarket.dto.validation.annotation.ValidRegion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class RegionValidator implements ConstraintValidator<ValidRegion, String> {

    private static final String REGION_PATTERN = "^(Ukraine|Poland|Germany|France|USA|Canada|UK|Netherlands|Japan)$";

    private static final Pattern pattern = Pattern.compile(REGION_PATTERN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
