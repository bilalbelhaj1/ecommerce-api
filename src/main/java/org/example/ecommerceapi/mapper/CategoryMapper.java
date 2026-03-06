package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.entity.Category;

import java.util.Objects;

/**
 * @author $(bilal belhaj)
 **/

public final class CategoryMapper {


    public static Category toEntity(CreateCategoryDTO createCategoryDTO) {
        return Category.builder()
                .name(createCategoryDTO.name())
                .description(createCategoryDTO.description())
                .build();
    }

    public static CategoryResponseDTO toDTO(Category category, int productCount) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productCount
        );
    }

    public static CategorySummaryDTO toSummary(Category category){
        return new CategorySummaryDTO(category.getName(), category.getId());
    }
}
