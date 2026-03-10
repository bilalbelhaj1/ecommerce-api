package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.entity.Rating;

/**
 * @author $(bilal belhaj)
 **/
public class RatingMapper {

    public static Rating toEntity(CreateRatingDTO dto, Customer customer, Product product) {
        return Rating.builder()
                .comment(dto.comment())
                .rating(dto.rating())
                .customer(customer)
                .product(product)
                .build();
    }

    public static RatingResponseDTO toDTO(Rating rating) {
        return new RatingResponseDTO(
                rating.getId(),
                rating.getComment(),
                rating.getRating(),
                CustomerMapper.toSummary(rating.getCustomer())
        );
    }
}
