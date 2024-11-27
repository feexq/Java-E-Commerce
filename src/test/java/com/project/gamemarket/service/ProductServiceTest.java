package com.project.gamemarket.service;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.CategoryType;
import com.project.gamemarket.common.KeyActivationStatus;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
import com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {ProductServiceImpl.class})
@DisplayName("Product Service Tests")
public class ProductServiceTest {

    private static final String KEY = "46717-H67O6-MBGRG";
    private static final String CUSTOMER_REFERENCE = "VLAD";
    private static final String PRODUCT_ID = "1";

    @Autowired
    private ProductService productService;

    @MockBean
    private KeyActivationService keyActivationService;

    @Test
    void shouldAddProduct() {
        ProductDetails newProduct =  buildProductDetailsMock();

        ProductDetails addedProduct = productService.addProduct(newProduct);

        assertNotNull(addedProduct.getId());
        assertEquals(newProduct.getTitle(), addedProduct.getTitle());
        assertEquals(newProduct.getShortDescription(), addedProduct.getShortDescription());
        assertEquals(newProduct.getPrice(), addedProduct.getPrice());
        assertEquals(newProduct.getDeveloper(), addedProduct.getDeveloper());
    }

    @Test
    void shouldFindProductById() {
        ProductDetails existingProduct = productService.getProducts().get(0);
        ProductDetails foundProduct = productService.getProductById(existingProduct.getId());

        assertEquals(existingProduct.getId(), foundProduct.getId());
        assertEquals(existingProduct.getTitle(), foundProduct.getTitle());
    }

    @Test
    void shouldThrowProductNotFoundException() {
        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(new Random().nextLong()));
    }

    @Test
    void shouldUpdateProduct() {
        ProductDetails existingProduct = productService.getProducts().get(0);
        ProductDetails updatedProduct = ProductDetails.builder()
                .id(existingProduct.getId())
                .title("Updated Title")
                .shortDescription("Updated Description")
                .price(25.0)
                .build();

        ProductDetails result = productService.updateProduct(updatedProduct);

        assertEquals(updatedProduct.getTitle(), result.getTitle());
        assertEquals(updatedProduct.getShortDescription(), result.getShortDescription());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getDeveloper(), result.getDeveloper());
    }

    @Test
    void shouldThrowProductNotFountExceptionUpdateProduct() {
        ProductDetails newProduct = buildProductDetailsMock().toBuilder().id(255L).build();

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(newProduct));

    }

    @Test
    void shouldActivateProduct(){
        KeyActivationRequestDto keyActivationRequestDto = KeyActivationRequestDto.builder().key(KEY).customerId(CUSTOMER_REFERENCE).build();
        KeyActivationResponseDto keyActivationResponseDto = KeyActivationResponseDto.builder().key(KEY).productId(PRODUCT_ID).status(KeyActivationStatus.SUCCESS).build();

        when(keyActivationService.processKeyActivation(eq(keyActivationRequestDto))).thenReturn(keyActivationResponseDto);

        ProductDetails result = productService.getProductByKeyActivation(keyActivationRequestDto);

        verify(keyActivationService, times(1)).processKeyActivation(keyActivationRequestDto);
        assertNotNull(result);
        assertEquals(buildProductDetailsMock(), result);

    }

    @Test
    void shouldThrowKeyActivationException(){
        KeyActivationRequestDto keyActivationRequestDto = KeyActivationRequestDto.builder().key(KEY).customerId(CUSTOMER_REFERENCE).build();
        KeyActivationResponseDto keyActivationResponseDto = KeyActivationResponseDto.builder().key(KEY).productId(PRODUCT_ID).status(KeyActivationStatus.EXPIRED).build();

        when(keyActivationService.processKeyActivation(eq(keyActivationRequestDto))).thenReturn(keyActivationResponseDto);

        assertThrows(KeyActivationFailedProcessActivation.class, () -> productService.getProductByKeyActivation(keyActivationRequestDto));
        verify(keyActivationService, times(1)).processKeyActivation(keyActivationRequestDto);

    }


    public ProductDetails buildProductDetailsMock() {
        return ProductDetails.builder()
                .id(1L)
                .title("Witcher 3")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("CD Projekt Red")
                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                .genres(List.of(CategoryType.RPG, CategoryType.MYTHOLOGY))
                .build();
    }


}
