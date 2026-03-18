package org.example.ecommerceapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomerSummaryDTO> register(@RequestBody CreateCustomerDTO dto){
       CustomerSummaryDTO res = authService.createAccount(dto);
       URI uri = URI.create("http://localhost:8081/api/v1/auth/login");
       return ResponseEntity.created(uri).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto){
        return ResponseEntity.ok().body(authService.login(dto));
    }
}
