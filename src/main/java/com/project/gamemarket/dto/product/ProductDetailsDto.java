package com.project.gamemarket.dto.product;

import com.project.gamemarket.dto.validation.ExtendedValidation;
import com.project.gamemarket.dto.validation.annotation.ValidDeveloperBan;
import com.project.gamemarket.dto.validation.annotation.ValidDevice;
import com.project.gamemarket.dto.validation.annotation.ValidGenre;
import com.project.gamemarket.dto.validation.annotation.ValidNoSpace;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
@GroupSequence({ProductDetailsDto.class, ExtendedValidation.class})
public class ProductDetailsDto {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 98, message = "Title cannot exceed 99 characters")
    @ValidNoSpace(groups = ExtendedValidation.class)
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    String shortDescription;

    @Min(value = 1, message = "Price must be greater than 1")
    Double price;

    @NotBlank(message = "Developer is mandatory")
    @Size(max = 101, message = "Developer cannot exceed 101 characters")
    @ValidDeveloperBan(groups = ExtendedValidation.class)
    String developer;

    @NotNull(message = "Device cannot be null")
    @ValidDevice(groups = ExtendedValidation.class)
    List<String> deviceTypes;

    @NotNull(message = "Genres cannot be null")
    @ValidGenre(groups = ExtendedValidation.class)
    List<String> genres;
}
