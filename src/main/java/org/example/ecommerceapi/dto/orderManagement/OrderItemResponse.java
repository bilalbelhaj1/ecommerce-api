package org.example.ecommerceapi.dto.orderManagement;

import java.math.BigDecimal;

/**
 * @author $(bilal belhaj)
 **/
public record OrderItemResponse(
        BigDecimal unitPrice,
        Integer quantity,
        String productName,
        Long productId
) {
}
