package org.example.ecommerceapi.dto.rating;

/**
 * @author $(bilal belhaj)
 **/
public record RatingResponseDTO(
        Long id,
        String comment,
        int rating,
        Long customerId
) {
}
