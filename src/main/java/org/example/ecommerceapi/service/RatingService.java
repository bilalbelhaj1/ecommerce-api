package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface RatingService {
    public RatingResponseDTO create(CreateRatingDTO dto);
    List<RatingResponseDTO> getAll(Long productId);
}
