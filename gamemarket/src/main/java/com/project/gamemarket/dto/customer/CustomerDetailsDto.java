package com.project.gamemarket.dto.customer;


import com.project.gamemarket.dto.validation.ExtendedValidation;
import com.project.gamemarket.dto.validation.annotation.ValidRegion;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
@GroupSequence({ CustomerDetailsDto.class, ExtendedValidation.class})
public class CustomerDetailsDto {

//    Integer age;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 99, message = "Name cannot exceed 99 characters")
    String name;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be a valid international format, e.g., +1234567890")
    String phoneNumber;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Region is mandatory")
    @Size(max = 255, message = "Region cannot exceed 366 characters")
    @ValidRegion(groups = ExtendedValidation.class)
    String region;


    List<String> deviceTypes;
}
