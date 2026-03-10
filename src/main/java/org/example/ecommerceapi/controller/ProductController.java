package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.service.ProductService;
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

    // get products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    // get one produc
    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> getOne(@PathVariable("productId") Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    // add product
    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody CreateProductDTO createProductDTO) {
        ProductResponseDTO res = productService.addProduct(createProductDTO);
        URI uri = URI.create("http://localhost:8080/api/v1/product/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // update product
    @PutMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody UpdateProductDTO updateProductDTO) {
        ProductResponseDTO res = productService.updateProduct(id, updateProductDTO);
        URI uri = URI.create("http://localhost:8080/api/v1/product/" + res.id());
        return ResponseEntity.ok().body(res);
    }

    // delete product
    @DeleteMapping(path = "{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
