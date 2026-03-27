package org.example.ecommerceapi.controller;

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
import tools.jackson.databind.ObjectMapper;

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
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile
    ){
        CreateProductDTO createProductDTO = mapper.readValue(productJson, CreateProductDTO.class);
        ProductResponseDTO res = productService.addProduct(createProductDTO, imageFile);
        logger.info("Request to create product, product name: {}", createProductDTO.name());
        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // get image of product
    @GetMapping("{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProduct(id);
        logger.info("Request to get product image productId={}", id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.imageType())).body(product.imageData());
    }

    // update product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable("productId") Long id,
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile
    ) {
        logger.info("Request to update product productId:{}", id);
        UpdateProductDTO updateProductDTO = mapper.readValue(productJson, UpdateProductDTO.class);
        ProductResponseDTO res = productService.updateProduct(id, updateProductDTO, imageFile);
        URI uri = URI.create("http://localhost:8080/api/v1/products/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // delete product (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        logger.info("Request to delete product productId:{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // get ratings
    @GetMapping("{id}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable Long id) {
        return ResponseEntity.ok().body(ratingService.getAll(id));
    }
}
