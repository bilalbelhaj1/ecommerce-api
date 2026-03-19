package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    // create
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/products/{productId}/rating")
    public ResponseEntity<RatingResponseDTO> rateProduct(@PathVariable Long productId, @Valid @RequestBody CreateRatingDTO dto) {
        RatingResponseDTO res = ratingService.create(productId, dto);
        URI uri = URI.create("http://localhost:8081/api/v1/ratings" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // list ratings
    @GetMapping("/products/{productId}/rating")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable Long productId) {
        return ResponseEntity.ok().body(ratingService.getAll(productId));
    }
}
