package org.example.ecommerceapi.dto.rating;

import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;

/**
 * @author $(bilal belhaj)
 **/
public record RatingResponseDTO(
        Long id,
        String comment,
        int rating,
        CustomerSummaryDTO customer
) {
}
