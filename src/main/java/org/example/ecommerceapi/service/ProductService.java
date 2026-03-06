package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.mapper.ProductMapper;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    // get Products
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    // get product by id
    public ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product Not found")
        );
        return ProductMapper.toDTO(product);
    }
    // add product
    public ProductResponseDTO addProduct(CreateProductDTO createProductDTO) {
        if (productRepository.existsByName(createProductDTO.name())) {
            throw new IllegalArgumentException("Product with the name : " + createProductDTO.name() + " Already exists");
        }
        Category cat = categoryRepository.findById(createProductDTO.categoryId()).orElseThrow(
                () -> new IllegalStateException("Category not found")
        );
        Product saved = productRepository.save(ProductMapper.toEntity(createProductDTO, cat));
        return ProductMapper.toDTO(saved);
    }
    // update product
    public ProductResponseDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product Not Found")
        );

        if (updateProductDTO.name() != null
                && !Objects.equals( updateProductDTO.name(), product.getName())
                && !updateProductDTO.name().isEmpty()
        ) {
            product.setName(updateProductDTO.name());
        }

        if (updateProductDTO.description() != null
                && !Objects.equals( updateProductDTO.description(), product.getDescription())
                && !updateProductDTO.description().isEmpty()
        ) {
            product.setDescription(updateProductDTO.description());
        }

        if (updateProductDTO.imageUrl() != null
                && !Objects.equals( updateProductDTO.imageUrl(), product.getImageUrl())
                && !updateProductDTO.imageUrl().isEmpty()
        ) {
            product.setImageUrl(updateProductDTO.imageUrl());
        }

        if (updateProductDTO.price() != null
                && updateProductDTO.price().compareTo(BigDecimal.valueOf(0.01)) > 0
                && !updateProductDTO.price().equals(product.getPrice())
        ) {
            product.setPrice(updateProductDTO.price());
        }

        if (updateProductDTO.stock() != null
                && updateProductDTO.stock() >= 0
                && !updateProductDTO.stock().equals(product.getStock())
        ) {
            product.setStock(updateProductDTO.stock());
        }
        if (updateProductDTO.categoryId() != null
                && !updateProductDTO.categoryId().equals(product.getCategory().getId())
        ) {
            Category category = categoryRepository.findById(updateProductDTO.categoryId()).orElseThrow(
                    () -> new IllegalStateException("Category Not Found")
            );
            product.setCategory(category);
        }
        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    // delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product not found")
        );
        productRepository.delete(product);
    }

}
