package com.project.gamemarket.service.mapper;

import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface KeyMapper {

    @Mapping(target = "key", source = "key")
    @Mapping(target = "customerId", source = "customerId")
    KeyActivationRequestDto toKeyContext(String customerId, String key);

}
