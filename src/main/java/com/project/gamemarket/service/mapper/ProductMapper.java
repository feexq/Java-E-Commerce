package com.project.gamemarket.service.mapper;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.CategoryType;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsEntry;
import com.project.gamemarket.dto.product.ProductDetailsListDto;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.repository.projection.OrderSummary;
import com.project.gamemarket.repository.projection.ProductSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "developer", source = "developer")
    @Mapping(target = "deviceTypes", source = "deviceTypes", qualifiedByName = "toDeviceTypeString")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "toGenresString")
    ProductDetailsDto toProductDetailsDto(ProductDetails productDetails);


    default ProductDetailsListDto toProductDetailsListDto(List<ProductDetails> productDetailsList) {
        return ProductDetailsListDto.builder()
                .productDetailsEntries((toProductDetailsEntry(productDetailsList)))
                .build();
    }

    List<ProductDetailsEntry> toProductDetailsEntry(List<ProductDetails> productDetails);


    @Mapping(target = "title", source = "title")
    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "developer", source = "developer")
    @Mapping(target = "deviceTypes", source = "deviceTypes", qualifiedByName = "toDeviceTypesFromString")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "toGenreTypesFromString")
    ProductDetails toProductDetails(ProductDetailsDto product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "product.title")
    @Mapping(target = "shortDescription", source = "product.shortDescription")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "developer", source = "product.developer")
    @Mapping(target = "deviceTypes", source = "product.deviceTypes", qualifiedByName = "toDeviceTypesFromString")
    @Mapping(target = "genres", source = "product.genres", qualifiedByName = "toGenreTypesFromString")
    ProductDetails toProductDetails(Long id, ProductDetailsDto product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    ProductDetailsEntry toProductDetailsEntry(ProductDetails productDetails);

    @Named("toDeviceTypeString")
    default List<String> toDeviceTypeString(List<DeviceType> devices) {
        return devices.stream().map(device -> device.name().toLowerCase()).toList();
    }

    @Named("toGenresString")
    default List<String> toGenresString(List<CategoryType> genres){
        return genres.stream().map(genre -> genre.name().toLowerCase()).toList();
    }

    @Named("toDeviceTypesFromString")
    default List<DeviceType> toDeviceTypesFromString(List<String> deviceTypes) {
        return deviceTypes.stream().map(DeviceType::fromName).toList();
    }

    @Named("toGenreTypesFromString")
    default List<CategoryType> toGenresStringFromString(List<String> genres) {
        return genres.stream().map(CategoryType::fromName).toList();
    }

    @Mapping(target = "device_type", source = "deviceTypes")
    @Mapping(target = "category_genre", source = "genres")
    ProductEntity toProductEntity(ProductDetails productDetails);

    @Mapping(source = "device_type", target = "deviceTypes")
    @Mapping(source = "category_genre", target = "genres")
    ProductDetails toProductDetails(ProductEntity productEntity);

    List<ProductDetails> toProductDetailsList(List<ProductEntity> productEntities);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(target = "price", source = "price")
    ProductDetails toProductFromProductSummary(ProductSummary productSummary);

}

