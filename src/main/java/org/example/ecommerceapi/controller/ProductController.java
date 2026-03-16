package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.service.ProductService;
import org.example.ecommerceapi.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RatingService ratingService;

    // get products
    @GetMapping
    public ResponseEntity<Page<ProductSummaryDTO>> getProducts(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return ResponseEntity.ok().body(productService.getProducts(status, categoryId, page, size));
    }

    // get All Products Admin
    @GetMapping("/all")
    public ResponseEntity<List<ProductSummaryDTO>> getAll() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    // get one product
    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> getOne(@PathVariable("productId") Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    // add product (admin)
    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody CreateProductDTO createProductDTO) {
        ProductResponseDTO res = productService.addProduct(createProductDTO);
        URI uri = URI.create("http://localhost:8080/api/v1/product/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // update product (admin)
    @PutMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody UpdateProductDTO updateProductDTO) {
        ProductResponseDTO res = productService.updateProduct(id, updateProductDTO);
        URI uri = URI.create("http://localhost:8080/api/v1/product/" + res.id());
        return ResponseEntity.ok().body(res);
    }

    // delete product (admin)
    @DeleteMapping(path = "{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // get ratings
    @GetMapping("/ratings/{id}")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(ratingService.getAll(id));
    }
}
