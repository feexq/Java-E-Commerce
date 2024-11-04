package com.project.gamemarket.dto.validation.annotation;

import com.project.gamemarket.dto.validation.validators.GenreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = GenreValidator.class)
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface ValidGenre {

    String GENRE_NOT_FOUND = "Genre type not found in the list of allowed genres. Example of a valid genre type: 'RPG', 'Action Roguelike', 'Hack and Slash', 'Mythology', 'Action', 'Adventure'.";

    String message() default GENRE_NOT_FOUND;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
