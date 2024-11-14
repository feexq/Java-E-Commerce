package com.project.gamemarket.domain;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.GenreType;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ProductDetails {

    Long id;
    String title;
    String shortDescription;
    Double price;
    String developer;
    List<DeviceType> deviceTypes;
    List<GenreType> genres;
}
