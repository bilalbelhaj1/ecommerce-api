package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface CategoryService {
    List<CategoryResponseDTO> getCategories();
    CategoryResponseDTO getCategory(Long id);
    CategoryResponseDTO addCategory(CreateCategoryDTO createCategoryDTO);
    CategoryResponseDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO);
    void deleteCategory(Long id);
    List<ProductSummaryDTO> getProductsByCategory(Long id);
}
