package org.example.ecommerceapi.dto.auth;

/**
 * @author $(bilal belhaj)
 **/
public record LoginResponseDTO(
        String fullName,
        Long id,
        String token
) {
}
