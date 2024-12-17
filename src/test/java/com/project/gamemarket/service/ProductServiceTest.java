package com.project.gamemarket.service;


import com.project.gamemarket.common.KeyActivationStatus;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
import com.project.gamemarket.featuretoggle.FeatureToggleService;
import com.project.gamemarket.featuretoggle.FeatureToggles;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation;
import com.project.gamemarket.service.impl.ProductServiceImpl;
import com.project.gamemarket.service.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {ProductServiceImpl.class})
@DisplayName("Product Service Tests")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private KeyActivationService keyActivationService;

    @MockBean
    private FeatureToggleService featureToggleService;

    @Autowired
    private ProductServiceImpl productService;

    private ProductDetails productDetails;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productDetails = ProductDetails.builder()
                .id(1L)
                .title("Test Game")
                .shortDescription("Test Description")
                .price(49.99)
                .developer("Test Developer")
                .build();

        productEntity = ProductEntity.builder()
                .id(1L)
                .title("test game")
                .shortDescription("Test Description")
                .price(49.99)
                .developer("Test Developer")
                .build();
    }

    @Test
    @DisplayName("Get Product by Key Activation - Expired Key")
    void testGetProductByKeyActivation_ExpiredKey() {
        KeyActivationRequestDto requestDto = KeyActivationRequestDto.builder().build();
        KeyActivationResponseDto responseDto = KeyActivationResponseDto.builder()
                .status(KeyActivationStatus.EXPIRED)
                .key("test-key")
                .build();

        when(keyActivationService.processKeyActivation(any(KeyActivationRequestDto.class))).thenReturn(responseDto);

        assertThrows(KeyActivationFailedProcessActivation.class, () ->
                productService.getProductByKeyActivation(requestDto)
        );
    }

    @Test
    @DisplayName("Get Sale Products - Halloween Sale")
    void testGetSaleProductByHoliday_Halloween() {
        when(featureToggleService.isFeatureEnabled(FeatureToggles.HALLOWEEN.getFeatureName())).thenReturn(true);
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productMapper.toProductDetailsList(anyList())).thenReturn(List.of(productDetails));

        List<ProductDetails> result = productService.getSaleProductByHoliday();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(44.991, result.get(0).getPrice(), 0.001); // 10% discount
    }

    @Test
    @DisplayName("Get Sale Products - Summer Sale")
    void testGetSaleProductByHoliday_SummerSale() {
        when(featureToggleService.isFeatureEnabled(FeatureToggles.HALLOWEEN.getFeatureName())).thenReturn(false);
        when(featureToggleService.isFeatureEnabled(FeatureToggles.SUMMER_SALE.getFeatureName())).thenReturn(true);
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productMapper.toProductDetailsList(anyList())).thenReturn(List.of(productDetails));

        List<ProductDetails> result = productService.getSaleProductByHoliday();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(42.4915, result.get(0).getPrice(), 0.001); // 15% discount
    }
}