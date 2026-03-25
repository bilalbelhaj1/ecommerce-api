package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface ProductService {
    List<ProductSummaryDTO> getAllProducts();
    Page<ProductSummaryDTO> getProducts(ProductStatus status, Long categoryId, int page, int size);
    Page<ProductSummaryDTO> search(String q, int page, int size);
    ProductResponseDTO getProduct(Long id);
    ProductResponseDTO addProduct(CreateProductDTO createProductDTO, MultipartFile imageFile);
    ProductResponseDTO updateProduct(Long id, UpdateProductDTO updateProductDTO, MultipartFile imageFile);
    void deleteProduct(Long id);
}
