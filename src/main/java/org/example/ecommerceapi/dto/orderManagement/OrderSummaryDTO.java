package org.example.ecommerceapi.dto.orderManagement;

import org.example.ecommerceapi.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author $(bilal belhaj)
 **/
public record OrderSummaryDTO(
        Long id,
        LocalDateTime date,
        String shippingAddress,
        BigDecimal totalAmount,
        OrderStatus status
) {
}
