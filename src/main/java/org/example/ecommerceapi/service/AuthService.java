package org.example.ecommerceapi.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author $(bilal belhaj)
 **/
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    // login
    public LoginResponseDTO login(LoginDTO dto) {
        Customer customer = customerRepository.findByEmail(dto.email()).orElseThrow(
                () -> new BadRequestException("Invalid email or password")
        );
        if (!passwordEncoder.matches(dto.password(), customer.getPasswordHash())) {
            throw new BadRequestException("Invalid password or email");
        }
        // create token (simulate)
        String token = "eokdoeddjedjejdijijfrhou";
        return new LoginResponseDTO(
                customer.getFirstName() + customer.getLastName(),
                customer.getId(),
                token
        );
    }

    // reset password

    // update password
}
