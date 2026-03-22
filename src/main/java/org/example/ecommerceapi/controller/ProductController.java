package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.service.ProductService;
import org.example.ecommerceapi.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RatingService ratingService;

    // get products
    @GetMapping
    public ResponseEntity<Page<ProductSummaryDTO>> getProducts(
            @RequestParam(defaultValue = "ACTIVE") ProductStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return ResponseEntity.ok().body(productService.getProducts(status, categoryId, page, size));
    }

    // search Products
    @GetMapping("search")
    public ResponseEntity<Page<ProductSummaryDTO>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(productService.search(q, page, size));
    }

    // get All Products Admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ProductSummaryDTO>> getAll() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    // get one product
    @GetMapping("{productId}")
    public ResponseEntity<ProductResponseDTO> getOne(@PathVariable("productId") Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    // add product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        CreateProductDTO createProductDTO =
                mapper.readValue(productJson, CreateProductDTO.class);

        ProductResponseDTO res = productService.addProduct(createProductDTO, imageFile);

        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // get image of product
    @GetMapping("{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProduct(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.imageType())).body(product.imageData());
    }

    // update product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable("productId") Long id,
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile) {
        ObjectMapper mapper = new ObjectMapper();
        UpdateProductDTO updateProductDTO = mapper.readValue(productJson, UpdateProductDTO.class);
        ProductResponseDTO res = productService.updateProduct(id, updateProductDTO, imageFile);
        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // delete product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // get ratings
    @GetMapping("{id}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable Long id) {
        return ResponseEntity.ok().body(ratingService.getAll(id));
    }
}
