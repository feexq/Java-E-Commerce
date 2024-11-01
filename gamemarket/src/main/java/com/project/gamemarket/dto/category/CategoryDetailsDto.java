package com.project.gamemarket.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CategoryDetailsDto {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100,message = "Title cannot exceed 100 characters")
    String name;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 366,message = "Title cannot exceed 366 characters")
    String description;
}
