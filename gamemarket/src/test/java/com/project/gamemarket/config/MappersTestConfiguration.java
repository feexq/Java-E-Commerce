package com.project.gamemarket.config;

import com.project.gamemarket.service.mapper.ProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;

public class MappersTestConfiguration {

    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }

}
