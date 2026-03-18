package org.example.ecommerceapi.dto.auth;

/**
 * @author $(bilal belhaj)
 **/
public record LoginResponseDTO(
        String email,
        Long id,
        String token
) {
}
