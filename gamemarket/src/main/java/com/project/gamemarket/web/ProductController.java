package com.project.gamemarket.web;

import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsListDto;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.mapper.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<ProductDetailsListDto> getProducts() {
        return ResponseEntity.ok(productMapper.toProductDetailsListDto(productService.getProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toProductDetailsDto(productService.getProductById(id)));
    }

   @PutMapping("/{id}")
   public ResponseEntity<ProductDetailsDto> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductDetailsDto product) {
        return ResponseEntity.ok(productMapper.toProductDetailsDto(productService.updateProduct(productMapper.toProductDetails(id,product))));
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDto> createProduct(@RequestBody @Valid ProductDetailsDto productDetailsDto) {
        return ResponseEntity.ok(productMapper.toProductDetailsDto(productService.addProduct(productMapper.toProductDetails(productDetailsDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/wiremock/{id}")
    public ResponseEntity<ProductDetailsDto> getProductByIdWiremock(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toProductDetailsDto(productService.getProductByIdWiremock(id)));
    }
}
