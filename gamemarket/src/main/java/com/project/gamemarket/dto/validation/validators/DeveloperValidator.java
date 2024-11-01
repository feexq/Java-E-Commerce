package com.project.gamemarket.dto.validation.validators;


import com.project.gamemarket.dto.validation.annotation.ValidDeveloperBan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class DeveloperValidator implements ConstraintValidator<ValidDeveloperBan, String> {

    private static final String BANNED_DEVELOPER_PATTERN =
            "(?i).*\\b(1C|Akella|Alawar|Buka Entertainment|Gaijin Entertainment|Game Insight|Nikita Online|Owlcat Games|Playrix|Russobit-M|Targem Games|VK|ZeptoLab)\\b.*";

    private static final Pattern pattern = Pattern.compile(BANNED_DEVELOPER_PATTERN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !pattern.matcher(value).matches();
    }
}
