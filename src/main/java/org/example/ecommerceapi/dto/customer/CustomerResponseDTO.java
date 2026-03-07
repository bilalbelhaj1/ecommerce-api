package org.example.ecommerceapi.dto.customer;

/**
 * @author $(bilal belhaj)
 **/
public record CustomerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address
) {
}
