package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.service.ProductService;
import org.example.ecommerceapi.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@Tag(name = "Products", description = "Operations related to products")
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RatingService ratingService;
    private final ObjectMapper mapper;
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    // get products
    @GetMapping
    @Operation(
            summary = "get all products",
            description = "public endpoint to get all products with pagination and filtering "
    )
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
    @Operation(
            summary = "Search for products",
            description = "Endpoint to search for products with pagination"
    )
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
    @Operation(
            summary = "get all products by admin",
            description = "admin endpoint to get all the products in the database"
    )
    public ResponseEntity<List<ProductSummaryDTO>> getAll() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    // get one product
    @GetMapping("{productId}")
    @Operation(
            summary = "Get one product by Id",
            description = "Returns full product details including ratings"
    )
    public ResponseEntity<ProductResponseDTO> getOne(
            @Parameter(description = "Id of the product") @PathVariable("productId") Long id
    ) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    // add product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "add new product",
            description = "admin endpoint to add new product"
    )
    public ResponseEntity<ProductResponseDTO> addProduct(
            @Parameter(description = "product data") @RequestPart("product") String productJson,
            @Parameter(description = "product image") @RequestPart("image") MultipartFile imageFile
    ) throws JsonProcessingException {
        CreateProductDTO createProductDTO = mapper.readValue(productJson, CreateProductDTO.class);
        ProductResponseDTO res = productService.addProduct(createProductDTO, imageFile);
        logger.info("Request to create product, product name: {}", createProductDTO.name());
        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // get image of product
    @GetMapping("{id}/image")
    @Operation(
            summary = "get product image",
            description = "get product image by product id"
    )
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProduct(id);
        logger.info("Request to get product image productId={}", id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.imageType())).body(product.imageData());
    }

    // update product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "{productId}")
    @Operation(
            summary = "update product",
            description = "Admin endpoint to update product data and image"
    )
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "product Id") @PathVariable("productId") Long id,
            @Parameter(description = "product new data") @RequestPart("product") String productJson,
            @Parameter(description = "optional new image") @RequestPart("image") MultipartFile imageFile
    ) throws JsonProcessingException {
        logger.info("Request to update product productId:{}", id);
        UpdateProductDTO updateProductDTO = mapper.readValue(productJson, UpdateProductDTO.class);
        ProductResponseDTO res = productService.updateProduct(id, updateProductDTO, imageFile);
        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // delete product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{productId}")
    @Operation(
            summary = "Delete product",
            description = "admin endpoint to delete product"
    )
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "Product Id") @PathVariable("productId") Long id) {
        logger.info("Request to delete product productId:{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // get ratings
    @GetMapping("{id}/ratings")
    @Operation(

            summary = "get product ratings",
            description = "returns all the product ratings"
    )
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@Parameter(description = "Product Id") @PathVariable Long id) {
        return ResponseEntity.ok().body(ratingService.getAll(id));
    }
}
