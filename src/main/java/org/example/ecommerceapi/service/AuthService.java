package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Role;
import org.example.ecommerceapi.entity.User;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.RoleRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * @author $(bilal belhaj)
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public CustomerSummaryDTO createAccount(CreateCustomerDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("User with this email already exists");
        }
        Role role = roleRepository.findByName("ROLE_CUSTOMER").orElse(roleRepository.save(Role.builder().name("ROLE_CUSTOMER").build()));
        User user = User.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .roles(new HashSet<>())
                .build();
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        Customer customer = CustomerMapper.ToEntity(dto, savedUser);
        return CustomerMapper.toSummary(customerRepository.save(customer)) ;
    }

    public LoginResponseDTO login(LoginDTO dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid email or password ")
        );
        if (!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new ResourceNotFoundException("Invalid email or password");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(user.getEmail(), user.getId(), token);
    }

}
