package org.example.ecommerceapi.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.entity.Rating;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.mapper.RatingMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    // create
    @Transactional
    public RatingResponseDTO create(Long productId, CreateRatingDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product Not found")
        );
        Rating rating = RatingMapper.toEntity(dto, customer, product);
        System.out.println(rating);
        Rating saved = ratingRepository.save(rating);
        return RatingMapper.toDTO(saved);
    }

    // get all
    public List<RatingResponseDTO> getAll(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product Not found")
        );
        return ratingRepository.findAllByProduct(product)
                .stream()
                .map(RatingMapper::toDTO)
                .toList();
    }
}
