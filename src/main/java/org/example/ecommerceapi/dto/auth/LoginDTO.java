package org.example.ecommerceapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author $(bilal belhaj)
 **/
public record LoginDTO(

        @NotBlank @Size(min = 8)
        String password,

        @NotBlank @Email
        String email
) {
}
