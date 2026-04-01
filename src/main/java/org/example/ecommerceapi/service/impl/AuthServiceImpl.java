package org.example.ecommerceapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Role;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.RoleRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.security.JwtService;
import org.example.ecommerceapi.service.AuthService;
import org.example.ecommerceapi.service.CustomerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * @author $(bilal belhaj)
 **/

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;

    public CustomerSummaryDTO createAccount(CreateCustomerDTO dto) {
        if (userRepository.existsByUsername(dto.email())) {
            throw new BadRequestException("User with this email already exists");
        }
        Role role = roleRepository.findByName("ROLE_CUSTOMER").orElse(roleRepository.save(Role.builder().name("ROLE_CUSTOMER").build()));
        AppUser appUser = AppUser.builder()
                .username(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .roles(new HashSet<>())
                .build();
        appUser.getRoles().add(role);
        AppUser savedAppUser = userRepository.save(appUser);
        return customerService.create(dto, savedAppUser);
    }

    public LoginResponseDTO login(LoginDTO dto) {
        AppUser appUser = userRepository.findByUsername(dto.email()).orElseThrow(
                () -> new BadRequestException("Invalid email or password ")
        );
        if (!passwordEncoder.matches(dto.password(), appUser.getPassword())){
            throw new BadRequestException("Invalid email or password");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        appUser.getUsername(), dto.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtService.generateToken(appUser);
        return new LoginResponseDTO(appUser.getUsername(), appUser.getId(), token);
    }

}
