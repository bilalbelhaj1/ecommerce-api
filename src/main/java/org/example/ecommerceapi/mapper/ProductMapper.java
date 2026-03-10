package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public final class ProductMapper {

    //create
    public static Product toEntity(CreateProductDTO createProductDTO, Category category) {
        return Product.builder()
                .name(createProductDTO.name())
                .description(createProductDTO.description())
                .price(createProductDTO.price())
                .stock(createProductDTO.stock())
                .imageUrl(createProductDTO.imageUrl())
                .category(category)
                .build();
    }

    public static ProductResponseDTO toDTO(Product product, List<RatingResponseDTO> ratings) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                CategoryMapper.toSummary(product.getCategory()),
                ratings

        );
    }

    public static ProductSummaryDTO toSummary(Product product, double rating, int nbrRatings) {
        return new ProductSummaryDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getImageUrl(),
                rating,
                nbrRatings
        );
    }
}
