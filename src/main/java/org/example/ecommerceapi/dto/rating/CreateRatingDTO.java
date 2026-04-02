package org.example.ecommerceapi.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author $(bilal belhaj)
 **/
public record CreateRatingDTO(
        @Min(1)
        Long productId,
        @Size(min = 4, max = 255)
        String comment,

        @Min(1) @Max(5)
        int rating,
        @Min(1)
        Long customerId
) {
}
