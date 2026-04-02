package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMIN", "CUSTOMER"})
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCustomers_returnsCustomers() throws Exception {

        List<CustomerResponseDTO> customers = List.of(
                new CustomerResponseDTO(1L, "bilal", "belhaj", "bilal@gmail.com", null, "tetouan"),
                new CustomerResponseDTO(2L, "bilal", "belhaj", "bil@gmail.com", null, "tetouan")
        );

        when(customerService.getCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())  // check HTTP 200
                .andExpect(jsonPath("$.length()").value(customers.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("bilal"))
                .andExpect(jsonPath("$[0].lastName").value("belhaj"))
                .andExpect(jsonPath("$[0].email").value("bilal@gmail.com"))
                .andExpect(jsonPath("$[0].address").value("tetouan"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("bil@gmail.com"));

    }

    @Test
    void getCustomer_returns_customer() throws Exception {
        CustomerResponseDTO customer = new CustomerResponseDTO(
                1L,
                "bilal",
                "belhaj",
                "bilal@gmail.com",
                null,
                "tetouan"
        );

        when(customerService.getProfile(1L)).thenReturn(customer);
        mockMvc.perform(get("/api/v1/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value(customer.firstName()))
                .andExpect(jsonPath("$.lastName").value(customer.lastName()))
                .andExpect(jsonPath("$.email").value(customer.email()))
                .andExpect(jsonPath("$.address").value(customer.address()))
                .andExpect(jsonPath("$.phoneNumber").isEmpty());
    }

    @Test
    void updateCustomer_returnsUpdatedCustomer() throws Exception {
        UpdateCustomerDTO req = new UpdateCustomerDTO(
                "newbilal",
                "belhaj",
                "bilal@gmail.com",
                null,
                "tetouan"
        );

        CustomerResponseDTO res = new CustomerResponseDTO(
                1L,
                "newbilal",
                "belhaj",
                "bilal@gmail.com",
                null,
                "tetouan"
        );

        when(customerService.updateProfile(1L, req)).thenReturn(res);

        mockMvc.perform(put("/api/v1/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.firstName").value(res.firstName()))
                .andExpect(jsonPath("$.lastName").value(res.lastName()))
                ;
    }


}