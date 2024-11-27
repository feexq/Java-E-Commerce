package com.project.gamemarket.service;

import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;

import java.util.List;

public interface ProductService {

    ProductDetails addProduct(ProductDetails product);

    ProductDetails updateProduct(ProductDetails product);

    void deleteProduct(Long id);

    ProductDetails getProductById(Long id);

    List<ProductDetails> getProducts();

    ProductDetails getProductByKeyActivation(KeyActivationRequestDto context);

    List<ProductDetails> getSaleProductByHoliday();
}
