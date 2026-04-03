package org.example.ecommerceapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
public class CustomerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerService customerService;

    @Test
    void getCustomers() throws Exception{
        List<Customer> customers = List.of(
                Customer.builder()
                        .email("bilal@gmail.com")
                        .address("tetouan")
                        .firstName("bilal")
                        .lastName("belhaj")
                        .appUser(AppUser.builder().password("password").username("bilal@gmail.com").build())
                        .build(),
                Customer.builder()
                        .email("belhaj@gmail.com")
                        .address("tetouan")
                        .firstName("bilal1")
                        .lastName("belhaj1")
                        .appUser(AppUser.builder().password("password").username("belhaj@gmail.com").build())
                        .build()
        );
        customerRepository.saveAll(customers);
        List<Customer> saved = customerRepository.findAll();

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(saved.size()))
                .andExpect(jsonPath("$[0].id").value(saved.get(0).getId()))
                .andExpect(jsonPath("$[0].email").value(saved.get(0).getEmail()))
                .andExpect(jsonPath("$[1].id").value(saved.get(1).getId()))
                .andExpect(jsonPath("$[1].email").value(saved.get(1).getEmail()));
    }

    @Test
    void getCustomer() throws Exception{
        Customer customer = customerRepository.save(
                Customer.builder()
                        .lastName("bel")
                        .firstName("bil")
                        .email("bilal@gmail.com")
                        .address("tetouan")
                        .appUser(AppUser.builder().username("bilal@gmaiil.com").password("password").build())
                        .build()
        );

        mockMvc.perform(get("/api/v1/customers/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer.getLastName()));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void updateCustomer() throws Exception{
        Customer customer = customerRepository.save(
                Customer.builder()
                        .lastName("bel")
                        .firstName("bil")
                        .email("bilal@gmail.com")
                        .address("tetouan")
                        .appUser(AppUser.builder().username("bilal@gmaiil.com").password("password").build())
                        .build()
        );

        UpdateCustomerDTO req = new UpdateCustomerDTO(
                "updatedName",
                "belhaj",
                "bilal@gmail.com",
                null,
                null
        );

        mockMvc.perform(put("/api/v1/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.firstName").value(req.firstName()))
                .andExpect(jsonPath("$.lastName").value(req.lastName()));
    }


}
