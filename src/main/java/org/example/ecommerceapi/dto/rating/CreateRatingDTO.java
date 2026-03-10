package org.example.ecommerceapi.dto.rating;

/**
 * @author $(bilal belhaj)
 **/
public record CreateRatingDTO(
        String comment,
        int rating,
        Long productId,
        Long customerId
) {
}
