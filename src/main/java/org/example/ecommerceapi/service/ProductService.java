package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.ProductMapper;
import org.example.ecommerceapi.mapper.RatingMapper;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // get All products
    public List<ProductSummaryDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> ProductMapper.toSummary(
                        product, productRepository.rating(product.getId()),
                        productRepository.nbrRatings(product)
                ))
                .toList();
    }

    // get Products
    public Page<ProductSummaryDTO> getProducts(ProductStatus status, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products = null;
        if(status != null && categoryId != null) {
            Category cat = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ResourceNotFoundException("Category not found")
            );
            products = productRepository.findByStatusAndCategory(status, cat, pageable);
        } else if (status != null) {
            products = productRepository.findByStatus(status, pageable);
        } else if (categoryId != null) {
            Category cat = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ResourceNotFoundException("Category Not found")
            );
            products = productRepository.findByCategory(cat, pageable);

        } else {
            products = productRepository.findByStatus(ProductStatus.ACTIVE, pageable);
        }
        return products
                .map(product -> ProductMapper.toSummary(product, productRepository.rating(product.getId()), productRepository.nbrRatings(product)));
    }

    // get product by id
    public ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product Not found")
        );
        List<RatingResponseDTO> ratings = !product.getRatings().isEmpty() ? product.getRatings().stream()
                .map(RatingMapper::toDTO)
                .toList() : new ArrayList<>();
        return ProductMapper.toDTO(product, ratings);
    }
    // add product
    public ProductResponseDTO addProduct(CreateProductDTO createProductDTO) {
        if (productRepository.existsByName(createProductDTO.name())) {
            throw new BadRequestException("Product with the name : " + createProductDTO.name() + " already exists");
        }
        Category cat = categoryRepository.findById(createProductDTO.categoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
        Product saved = productRepository.save(ProductMapper.toEntity(createProductDTO, cat));
        List<RatingResponseDTO> ratings = !saved.getRatings().isEmpty() ? saved.getRatings().stream()
                .map(RatingMapper::toDTO)
                .toList() : new ArrayList<>();
        return ProductMapper.toDTO(saved, ratings);
    }
    // update product
    public ProductResponseDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product Not Found")
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
                    () -> new ResourceNotFoundException("Category Not Found")
            );
            product.setCategory(category);
        }
        Product saved = productRepository.save(product);
        List<RatingResponseDTO> ratings = !saved.getRatings().isEmpty() ? saved.getRatings().stream()
                .map(RatingMapper::toDTO)
                .toList() : new ArrayList<>();
        return ProductMapper.toDTO(saved, ratings);
    }

    // delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        productRepository.delete(product);
    }

}
