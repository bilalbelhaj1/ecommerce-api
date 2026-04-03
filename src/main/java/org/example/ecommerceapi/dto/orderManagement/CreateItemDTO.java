package org.example.ecommerceapi.dto.orderManagement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * @author $(bilal belhaj)
 **/
public record CreateItemDTO(
        @Min(1)
        Long productId,
        @Positive @Min(1)
        Integer quantity
) {
}
