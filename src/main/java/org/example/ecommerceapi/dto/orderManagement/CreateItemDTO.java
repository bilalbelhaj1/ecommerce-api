package org.example.ecommerceapi.dto.orderManagement;

/**
 * @author $(bilal belhaj)
 **/
public record CreateItemDTO(
        Long productId,
        Integer quantity
) {
}
