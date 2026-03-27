package org.example.ecommerceapi.dto.orderManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public record CreateOrderDTO(
        @NotBlank
        Long customerId,
        @NotEmpty
        List<CreateItemDTO> items,
        @NotBlank @Size(min = 3)
        String shippingAddress
) {
}
