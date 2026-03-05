package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // get categories
    public List<CategoryResponseDTO> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat-> new CategoryResponseDTO(
                        cat.getId(),
                        cat.getName(),
                        cat.getDescription(),
                        productRepository.countByCategoryId(cat.getId()))
                )
                .toList();
    }
    // getCategory
    public CategoryResponseDTO getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new IllegalStateException("Category not found"));
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productRepository.countByCategoryId(category.getId()));
    }

    // add category
    public CategoryResponseDTO addCategory(CreateCategoryDTO createCategoryDTO) {
        // check if category name already exists
        if (categoryRepository.existsByName(createCategoryDTO.name())) {
            throw new IllegalArgumentException("Category with the name" + createCategoryDTO.name() + "already exists");
        }
        // map to entity
        Category category = Category.builder()
                .name(createCategoryDTO.name())
                .description(createCategoryDTO.description())
                .build();
        // save
        Category saved = categoryRepository.save(category);
        return new CategoryResponseDTO(saved.getId(), saved.getName(), saved.getDescription(), 0);
    }

    // update category
    public void updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("category does not exists"));
        if (updateCategoryDTO.name() != null
                && !updateCategoryDTO.name().isEmpty()
                && !Objects.equals(updateCategoryDTO.name(), category.getName())){
            category.setName(updateCategoryDTO.name());
        }
        if (updateCategoryDTO.description() != null
                && !updateCategoryDTO.description().isEmpty()
                && !Objects.equals(updateCategoryDTO.description(), category.getDescription())){
            category.setDescription(updateCategoryDTO.description());
        }
        categoryRepository.save(category);
    }

    // delete category
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new IllegalStateException("Category not found"));
        categoryRepository.delete(category);
    }
}
