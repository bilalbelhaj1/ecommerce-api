package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ratings", description = "Operations related to ratings")
@RestController
@RequestMapping("api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    // create
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping()
    @Operation(
            summary = "rate product",
            description = "add a rating to a product"
    )
    public ResponseEntity<RatingResponseDTO> rateProduct(@Valid @RequestBody CreateRatingDTO dto) {
        RatingResponseDTO res = ratingService.create(dto);
        URI uri = URI.create("http://localhost:8081/api/v1/ratings/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // list ratings
    @GetMapping("product/{productId}")
    @Operation(
            summary = "get ratings",
            description = "get all ratings of a product"
    )
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@Parameter(description = "Product Id") @PathVariable Long productId) {
        return ResponseEntity.ok().body(ratingService.getAll(productId));
    }
}
