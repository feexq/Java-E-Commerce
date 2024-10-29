package com.project.gamemarket.service;

import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.product.ProductDetailsDto;

import java.util.List;

public interface ProductService {

    ProductDetails addProduct(ProductDetails product);

    ProductDetails updateProduct(ProductDetails product);

    ProductDetails deleteProduct(Long id);

    ProductDetails getProductById(Long id);

    List<ProductDetails> getProducts();
}
