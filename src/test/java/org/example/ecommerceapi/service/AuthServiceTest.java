package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Role;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.repository.RoleRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.security.JwtService;
import org.example.ecommerceapi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private CustomerService customerService;
    private final String email = "bilal@gmail.com";
    private final String password = "password";
    @InjectMocks private AuthServiceImpl underTest;

    @Test
    void createAccount() {
        CreateCustomerDTO dto = new CreateCustomerDTO(
                "bilal",
                "belhaj",
                email,
                password,
                "tetouan",
                null
        );
        Role role = Role.builder().name("ROLE_CUSTOMER").build();
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(AppUser.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        when(customerService.create(any(), any())).thenReturn(new CustomerSummaryDTO(1L, "bilal", "belhaj"));

        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        CustomerSummaryDTO res = underTest.createAccount(dto);
        verify(userRepository).save(appUserArgumentCaptor.capture());
        AppUser saved = appUserArgumentCaptor.getValue();

        assertThat(res).isNotNull();
        assertThat(saved.getUsername()).isEqualTo(email);
        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    void createAccount_throws_whenEmailExists() {
        CreateCustomerDTO dto = new CreateCustomerDTO(
                "bilal",
                "belhaj",
                email,
                password,
                "tetouan",
                null
        );
        when(userRepository.existsByUsername(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.createAccount(dto)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void login() {
    }
}