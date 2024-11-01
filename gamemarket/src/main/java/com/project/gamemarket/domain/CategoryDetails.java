package com.project.gamemarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CategoryDetails {

    UUID id;
    String name;
    String description;
}
