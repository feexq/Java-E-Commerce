package com.project.gamemarket.web;

import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.product.ProductDetailsDto;
import com.project.gamemarket.dto.product.ProductDetailsListDto;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.mapper.KeyMapper;
import com.project.gamemarket.service.mapper.ProductMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final KeyMapper keyMapper;

    public ProductController(ProductService productService, ProductMapper productMapper, KeyMapper keyMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.keyMapper = keyMapper;
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

    @PostMapping("/{customerReference}/activate")
    public ResponseEntity<ProductDetailsDto> getProductByKeyActivation(
            @PathVariable("customerReference") String customerReference,
            @RequestBody @NotBlank String key) {
        log.info("Getting product for key: {}", key);
        ProductDetails response = productService.getProductByKeyActivation(keyMapper.toKeyContext(customerReference,key));
        return ResponseEntity.ok(productMapper.toProductDetailsDto(response));
    }
}
