package org.example.ecommerceapi.dto.product;

import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Category;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        CategorySummaryDTO category,
        List<RatingResponseDTO> ratings
) {
}
