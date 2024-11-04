package com.project.gamemarket.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ProductDetailsEntry {

    Long id;
    String title;
    String shortDescription;
    Double price;
    String developer;
    List<String> deviceTypes;
    List<String> genres;
}
