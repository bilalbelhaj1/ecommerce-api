package org.example.ecommerceapi.dto.product;

import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.entity.Category;

import java.math.BigDecimal;

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
        CategorySummaryDTO category
) {
}
