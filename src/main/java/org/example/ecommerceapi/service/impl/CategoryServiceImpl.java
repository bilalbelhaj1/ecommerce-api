package org.example.ecommerceapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CategoryMapper;
import org.example.ecommerceapi.mapper.ProductMapper;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // get categories
    public List<CategoryResponseDTO> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat -> CategoryMapper.toDTO(cat, productRepository.countByCategoryId(cat.getId())))
                .toList();
    }
    // getCategory
    public CategoryResponseDTO getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        return  CategoryMapper.toDTO(
                category,
                productRepository.countByCategoryId(category.getId()));
    }

    // add category
    public CategoryResponseDTO addCategory(CreateCategoryDTO createCategoryDTO) {
        // check if category name already exists
        if (categoryRepository.existsByName(createCategoryDTO.name())) {
            throw new BadRequestException("Category with the name " + createCategoryDTO.name() + " already exists");
        }
        System.out.println(createCategoryDTO);
        // map to entity
        Category category = CategoryMapper.toEntity(createCategoryDTO);
        // save
        Category saved = categoryRepository.save(category);
        return new CategoryResponseDTO(saved.getId(), saved.getName(), saved.getDescription(), 0);
    }

    // update category
    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("category does not exists"));
        if (updateCategoryDTO.name() != null
                && !updateCategoryDTO.name().isEmpty()
                && !Objects.equals(updateCategoryDTO.name(), category.getName())){
            if (categoryRepository.existsByName(updateCategoryDTO.name())) {
                throw new BadRequestException("Category with name " + updateCategoryDTO.name() + " Already exists ");
            }
            category.setName(updateCategoryDTO.name());
        }
        if (updateCategoryDTO.description() != null
                && !updateCategoryDTO.description().isEmpty()
                && !Objects.equals(updateCategoryDTO.description(), category.getDescription())){
            category.setDescription(updateCategoryDTO.description());
        }
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDTO(saved, productRepository.countByCategoryId(saved.getId()));
    }

    // delete category
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    // get category products
    public List<ProductSummaryDTO> getProductsByCategory(Long id) {
        Category category  = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
        return category.getProducts().stream()
                .map(product -> ProductMapper.toSummary(product, productRepository.rating(product.getId()), productRepository.nbrRatings(product)))
                .toList();
    }
}
