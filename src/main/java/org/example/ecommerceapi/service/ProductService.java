package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Service
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
                        p.getCategory()))
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
                product.getCategory());
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
                saved.getCategory()
        );
    }
    // update product

    // delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Product not found")
        );
        productRepository.delete(product);
    }

}
