package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
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
                .map(p-> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getImageUrl(),
                        new CategorySummaryDTO(
                                p.getCategory().getName(),
                                p.getCategory().getId()
                        )))
                .toList();
    }

    // get product by id
    public ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product Not found")
        );
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                new CategorySummaryDTO(
                        product.getCategory().getName(),
                        product.getCategory().getId()
                ));
    }
    // add product
    public ProductResponseDTO addProduct(CreateProductDTO createProductDTO) {
        if (productRepository.existsByName(createProductDTO.name())) {
            throw new IllegalArgumentException("Product with the name : " + createProductDTO.name() + " Already exists");
        }
        Category cat = categoryRepository.findById(createProductDTO.categoryId()).orElseThrow(
                () -> new IllegalStateException("Category not found")
        );
        Product product = Product.builder()
                .name(createProductDTO.name())
                .description(createProductDTO.description())
                .price(createProductDTO.price())
                .stock(createProductDTO.stock())
                .imageUrl(createProductDTO.imageUrl())
                .category(cat)
                .build();
        Product saved = productRepository.save(product);
        return new ProductResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock(),
                saved.getImageUrl(),
                new CategorySummaryDTO(
                        saved.getCategory().getName(),
                        saved.getCategory().getId()
                )
        );
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
        return new ProductResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock(),
                saved.getImageUrl(),
                new CategorySummaryDTO(
                        saved.getCategory().getName(),
                        saved.getCategory().getId()
                )
        );
    }

    // delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product not found")
        );
        productRepository.delete(product);
    }

}
