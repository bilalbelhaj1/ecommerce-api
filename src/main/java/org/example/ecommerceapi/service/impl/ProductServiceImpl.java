package org.example.ecommerceapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.example.ecommerceapi.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

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

    // search Products
    public Page<ProductSummaryDTO> search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.searchProducts(q, pageable).map(
                product -> ProductMapper.toSummary(product, productRepository.rating(product.getId()), productRepository.nbrRatings(product))
        );
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
    public ProductResponseDTO addProduct(CreateProductDTO createProductDTO, MultipartFile imageFile) {
        if (productRepository.existsByName(createProductDTO.name())) {
            logger.warn("Product with name {} already exists", createProductDTO.name());
            throw new BadRequestException("Product with the name : " + createProductDTO.name() + " already exists");
        }
        Category cat = categoryRepository.findById(createProductDTO.categoryId()).orElseThrow(

        () -> {
            logger.warn("Product with name {} not created category not found categoryId: {}", createProductDTO.name(), createProductDTO.categoryId());
            return new ResourceNotFoundException("Category not found");
        }
        );
        if (imageFile == null) {
            logger.warn("Product with name {} not created, no image provided", createProductDTO.name());
            throw new BadRequestException("Product image is required");
        }
        Product product = ProductMapper.toEntity(createProductDTO, cat);
        try {
            product.setImageData(imageFile.getBytes());
            product.setImageType(imageFile.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Product saved = productRepository.save(product);

        List<RatingResponseDTO> ratings = !saved.getRatings().isEmpty() ? saved.getRatings().stream()
                .map(RatingMapper::toDTO)
                .toList() : new ArrayList<>();
        logger.info("Product with name {} created productId: {}", saved.getName(), saved.getId());
        return ProductMapper.toDTO(saved, ratings);
    }

    // update product
    public ProductResponseDTO updateProduct(Long id, UpdateProductDTO dto, MultipartFile imageFile) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Product not updated product not found productId: {}", id);
                    return new ResourceNotFoundException("Product Not Found");
                }
        );
        Product saved = productRepository.save(update(product, dto, imageFile ));
        List<RatingResponseDTO> ratings = !saved.getRatings().isEmpty() ? saved.getRatings().stream()
                .map(RatingMapper::toDTO)
                .toList() : new ArrayList<>();
        logger.info("Product updated productId: {}", id);
        return ProductMapper.toDTO(saved, ratings);
    }

    // delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Product not found productId: {}", id);
                    return new ResourceNotFoundException("Product not found");
                }
        );
        logger.info("Product deleted ");
        productRepository.delete(product);
    }

    private Product update(Product product, UpdateProductDTO dto, MultipartFile imageFile) {
        if (dto.name() != null
                && !Objects.equals( dto.name(), product.getName())
                && !dto.name().isEmpty()
        ) {
            product.setName(dto.name());
        }

        if (dto.description() != null
                && !Objects.equals( dto.description(), product.getDescription())
                && !dto.description().isEmpty()
        ) {
            product.setDescription(dto.description());
        }

        if (imageFile != null) {
            try {
                product.setImageData(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (dto.price() != null
                && dto.price().compareTo(BigDecimal.valueOf(0.01)) > 0
                && !dto.price().equals(product.getPrice())
        ) {
            product.setPrice(dto.price());
        }

        if (dto.stock() != null
                && dto.stock() >= 0
                && !dto.stock().equals(product.getStock())
        ) {
            product.setStock(dto.stock());
        }
        if (dto.categoryId() != null
                && !dto.categoryId().equals(product.getCategory().getId())
        ) {
            Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(
                    () -> new ResourceNotFoundException("Category Not Found")
            );
            product.setCategory(category);
        }
        return product;
    }

}
