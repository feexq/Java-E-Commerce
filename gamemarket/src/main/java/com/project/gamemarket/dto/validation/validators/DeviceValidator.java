package com.project.gamemarket.dto.validation.validators;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.dto.validation.annotation.ValidDevice;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class DeviceValidator implements ConstraintValidator<ValidDevice, List<String>> {

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        try {
            for (String value : values) {
                DeviceType.fromName(value);
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}
