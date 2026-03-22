package org.example.ecommerceapi.dto.product;

import java.math.BigDecimal;

/**
 * @author $(bilal belhaj)
 **/
public record ProductSummaryDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        String description,
        byte[] imageData,
        Double rating,
        Integer nbrRatings
) {
}
