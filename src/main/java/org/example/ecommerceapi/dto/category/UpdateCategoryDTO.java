package org.example.ecommerceapi.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author $(bilal belhaj)
 **/
public record UpdateCategoryDTO(
        @NotBlank @Size(min = 2, max = 100)
        String name,
        String description
) {
}
