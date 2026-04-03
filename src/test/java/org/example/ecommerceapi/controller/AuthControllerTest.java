package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc

class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void register() throws Exception{
        CustomerSummaryDTO res = new CustomerSummaryDTO(
                1L, "bilal", "belhaj"
        );
        CreateCustomerDTO req = new CreateCustomerDTO(
                "bilal",
                "belhaj",
                "bilal@gmail.com",
                "password",
                "tetouan",
                null
        );

        when(authService.createAccount(req)).thenReturn(res);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("bilal"))
                .andExpect(jsonPath("$.lastName").value("belhaj"));
    }

    @Test
    void login() throws Exception{
        LoginDTO req = new LoginDTO("password", "bilal@gmail.com");
        LoginResponseDTO res = new LoginResponseDTO("bilal@gmail.com", 1L, "token");

        when(authService.login(req)).thenReturn(res);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("bilal@gmail.com"))
                .andExpect(jsonPath("$.token").value("token"));
    }
}