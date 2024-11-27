package com.project.gamemarket.service.impl;

import com.project.gamemarket.common.KeyActivationStatus;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
import com.project.gamemarket.featuretoggle.FeatureToggleService;
import com.project.gamemarket.featuretoggle.FeatureToggles;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.service.KeyActivationService;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.mapper.ProductMapper;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final KeyActivationService keyActivationService;
    private final FeatureToggleService featureToggleService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDetails addProduct(ProductDetails product) {
        try {
            return productMapper.toProductDetails(productRepository.save(productMapper.toProductEntity(product)));
        } catch (Exception e) {
            log.error("Error while adding product");
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public ProductDetails updateProduct(ProductDetails product) {
        return productMapper.toProductDetails(
                productRepository.save(
                        productMapper.toProductEntity(
                                getProductById(product.getId()).toBuilder()
                                .id(product.getId())
                                .title(product.getTitle())
                                .shortDescription(product.getShortDescription())
                                .price(product.getPrice())
                                .developer(product.getDeveloper())
                                .deviceTypes(product.getDeviceTypes())
                                .genres(product.getGenres())
                                .build())));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while deleting product with id {}", id);
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public ProductDetails getProductByKeyActivation(KeyActivationRequestDto context) {
        log.info("Getting product for with key: {}", context.getCustomerId());
        KeyActivationResponseDto keyActivationResponseDto = keyActivationService.processKeyActivation(context);
        if (KeyActivationStatus.EXPIRED.equals(keyActivationResponseDto.getStatus())
                || KeyActivationStatus.NOT_EXISTENT.equals(keyActivationResponseDto.getStatus())) {
            throw new KeyActivationFailedProcessActivation(keyActivationResponseDto.getKey().replace("\"", ""));
        }
        Long ID = Long.parseLong(keyActivationResponseDto.getProductId());
        try {
            return productMapper.toProductDetails(productRepository.findById(ID).orElseThrow(() -> {
                log.error("Error while getting product with id {}", ID);
                return new ProductNotFoundException(ID);
            }));
        } catch (Exception e) {
            log.error("Error while getting product");
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public List<ProductDetails> getSaleProductByHoliday() {
        double discountPercentage = 0.0;
        if (featureToggleService.isFeatureEnabled(FeatureToggles.HALLOWEEN.getFeatureName())) {
            discountPercentage = 10.0;
        } else if (featureToggleService.isFeatureEnabled(FeatureToggles.SUMMER_SALE.getFeatureName())) {
            discountPercentage = 15.0;
        }
        return getSaleProducts(discountPercentage);
    }

    @Transactional
    public List<ProductDetails> getSaleProducts(double discountPercentage) {
        return productMapper.toProductDetailsList(productRepository.findAll()).stream()
                .map(product -> ProductDetails.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .shortDescription(product.getShortDescription())
                        .price(product.getPrice() * (1 - discountPercentage / 100))
                        .developer(product.getDeveloper())
                        .deviceTypes(product.getDeviceTypes())
                        .genres(product.getGenres())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public ProductDetails getProductById(Long id) {
        try {
            return productMapper.toProductDetails(productRepository.findById(id).orElseThrow(() -> {
                log.error("Error while getting product with id {}", id);
                return new ProductNotFoundException(id);
            }));
        } catch (Exception e) {
            log.error("Error while getting product");
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public List<ProductDetails> getProducts() {
        return productMapper.toProductDetailsList(productRepository.findAll());
    }

}
