package com.project.gamemarket.dto.validation.annotation;


import com.project.gamemarket.dto.validation.validators.RegionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = RegionValidator.class)
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface ValidRegion {

    String REGION_SHOULD_BE_VALID = "The region is not valid. Allowed regions: Ukraine, Poland, Germany, France, USA, Canada, UK, Netherlands, Japan.";

    String message() default REGION_SHOULD_BE_VALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
