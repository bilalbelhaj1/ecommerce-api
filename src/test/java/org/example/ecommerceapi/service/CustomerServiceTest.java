package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 *
 **/
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerRepository customerRepository;
    @InjectMocks private CustomerServiceImpl underTest;

    private Customer customer;
    private AppUser user;
    private CreateCustomerDTO createDto;
    private UpdateCustomerDTO updateDto;

    private final String email = "bilal@gmail.com";

    @BeforeEach
    void setUp() {
        user = AppUser.builder().username(email).password("password").build();

        customer = Customer.builder()
                .id(1L)
                .firstName("Bilal")
                .lastName("Belhaj")
                .email(email)
                .address("Tetouan")
                .phoneNumber("0678787878")
                .appUser(user)
                .build();

        createDto = new CreateCustomerDTO(
                "Bilal", "Belhaj", email, "password", "Tetouan", "0678787878"
        );

        updateDto = new UpdateCustomerDTO(
                "Bilal", "Belhaj", "newemail@gmail.com", "New Address", "0678999999"
        );
    }

    @Test
    void getCustomers_callsRepository() {
        underTest.getCustomers();
        verify(customerRepository).findAll();
    }

    @Test
    void createCustomer_savesAndReturnsDTO_whenEmailNotExists() {
        when(customerRepository.existsByEmail(email)).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        CustomerSummaryDTO result = underTest.create(createDto, user);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());

        Customer saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void createCustomer_throws_whenEmailExists() {
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.create(createDto, user))
                .isInstanceOf(BadRequestException.class);

        verify(customerRepository, never()).save(any());
    }

    @Test
    void getProfile_returnsCustomer_whenExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = underTest.getProfile(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(customer.getId());
    }

    @Test
    void getProfile_throws_whenNotExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getProfile(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProfile_updatesCustomer_whenExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateDto.email())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponseDTO result = underTest.updateProfile(1L, updateDto);

        assertThat(result.id()).isEqualTo(customer.getId());
        assertThat(result.email()).isEqualTo(updateDto.email());
        assertThat(result.address()).isEqualTo(updateDto.address());
    }

    @Test
    void updateProfile_throws_whenCustomerNotExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateProfile(1L, updateDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProfile_throws_whenEmailAlreadyExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateDto.email())).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateProfile(1L, updateDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void deleteAccount_deletesCustomer_whenExists() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        underTest.deleteAccount(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteAccount_throws_whenNotExists() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteAccount(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(customerRepository, never()).deleteById(1L);
    }
}