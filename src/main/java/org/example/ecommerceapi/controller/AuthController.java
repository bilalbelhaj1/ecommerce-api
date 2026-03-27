package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Role;
import org.example.ecommerceapi.repository.RoleRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashSet;

/**
 * @author $(bilal belhaj)
 **/

@Tag(name = "Auth", description = "Authentication operations")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/create-admin")
    @Operation(
            summary = "create admin",
            description = "create new admin (for test purposes)"
    )
    public AppUser createAdmin(
            @RequestParam String email,
            @RequestParam String password
    ) {
        Role role = roleRepository.findByName("ROLE_ADMIN").orElse(
                Role.builder()
                        .name("ROLE_ADMIN").build()
        );
        Role saved = roleRepository.save(role);
        AppUser appUser = AppUser.builder()
                .username(email)
                .password(passwordEncoder.encode(password))
                .roles(new HashSet<>())
                .build();
        appUser.getRoles().add(saved);
        return userRepository.save(appUser);
    }

    @PostMapping("/register")
    @Operation(
            summary = "create account",
            description = "create new account"
    )
    public ResponseEntity<CustomerSummaryDTO> register(@RequestBody CreateCustomerDTO dto){
       CustomerSummaryDTO res = authService.createAccount(dto);
       URI uri = URI.create("http://localhost:8081/api/v1/auth/login");
       return ResponseEntity.created(uri).body(res);
    }

    @PostMapping("/login")
    @Operation(
            summary = "login",
            description = "login "
    )
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto){
        return ResponseEntity.ok().body(authService.login(dto));
    }
}
