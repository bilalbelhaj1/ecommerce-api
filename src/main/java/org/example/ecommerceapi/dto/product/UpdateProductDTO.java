package org.example.ecommerceapi.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * @author $(bilal belhaj)
 **/
public record UpdateProductDTO(
        @NotBlank @Size(min = 2, max = 100)
        String name,
        String description,
        @DecimalMin("0.01")
        BigDecimal price,
        @PositiveOrZero
        Integer stock,
        String imageUrl,
        Long categoryId
) {
}
