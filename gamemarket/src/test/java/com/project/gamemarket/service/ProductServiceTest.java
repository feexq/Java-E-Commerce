package com.project.gamemarket.service;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.GenreType;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {ProductServiceImpl.class})
@DisplayName("Product Service Tests")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductFindService productFindService;

    private final Long ID = new Random().nextLong();

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
        ProductDetails newProduct = buildProductDetailsMock();

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(newProduct));

    }

    @Test
    void shouldFindByIdWiremock(){
        ProductDetails buildProduct = buildProductDetailsMock();

        when(productFindService.processFinding(eq(ID))).thenReturn(buildProduct);

        ProductDetails result = productService.getProductByIdWiremock(ID);

        verify(productFindService, times(1)).processFinding(ID);
        assertNotNull(result);

    }

    @Test
    void shouldThrowFindByIdWiremock(){
        ProductDetails buildProduct = buildProductDetailsMock();

        when(productFindService.processFinding(eq(ID))).thenReturn(buildProduct);

        ProductDetails result = productService.getProductByIdWiremock(ID);

        verify(productFindService, times(1)).processFinding(ID);
        assertNotNull(result);

    }


    public ProductDetails buildProductDetailsMock() {
        return ProductDetails.builder()
                .id(ID)
                .title("Witcher 3")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("CD Projekt Red")
                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                .genres(List.of(GenreType.RPG,GenreType.MYTHOLOGY))
                .build();
    }


}
