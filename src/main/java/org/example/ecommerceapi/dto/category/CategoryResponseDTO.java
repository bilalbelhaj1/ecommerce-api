package org.example.ecommerceapi.dto.category;

/**
 * @author $(bilal belhaj)
 **/
public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        int productsNbr
) {
}
