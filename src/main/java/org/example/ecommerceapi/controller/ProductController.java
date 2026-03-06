package org.example.ecommerceapi.controller;

import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // get products
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productService.getAllProducts();
    }

    // get one produc
    @GetMapping(path = "{productId}")
    public ProductResponseDTO getOne(@PathVariable("productId") Long id) {
        return productService.getProduct(id);
    }

    // add product
    @PostMapping
    public ProductResponseDTO addProduct(@RequestBody CreateProductDTO createProductDTO) {
        return productService.addProduct(createProductDTO);
    }

    // update product
    @PutMapping(path = "{productId}")
    public ProductResponseDTO updateProduct(@PathVariable("productId") Long id, @RequestBody UpdateProductDTO updateProductDTO) {
        return productService.updateProduct(id, updateProductDTO);
    }

    // delete product
    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
    }
}
