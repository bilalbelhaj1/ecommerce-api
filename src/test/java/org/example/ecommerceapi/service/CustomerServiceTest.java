package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void CanGetCustomers() {
        // when
        underTest.getCustomers();
        // then
        verify(customerRepository).findAll();
    }

    @Test
    void canAddCustomer() {
        // given
        String email = "bilal@gmail.com";
        String password = "bilalbelhaj";

        AppUser user = AppUser.builder()
                .username(email)
                .password(passwordEncoder.encode(password))
                .build();

        CreateCustomerDTO dto = new CreateCustomerDTO(
                "bilal", "belhaj", email, "bilalbelhaj", "tetouan", "0678787878"
        );

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        underTest.create(dto, user);

        // then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured)
                .usingRecursiveComparison()
                .isEqualTo(CustomerMapper.ToEntity(dto, user));
    }

    @Test
    void throwsWhenEmailExists() {
        // given
        String email = "bilal@gmail.com";
        String password = "bilalbelhaj";

        AppUser user = AppUser.builder()
                .username(email)
                .password(passwordEncoder.encode(password))
                .build();

        CreateCustomerDTO dto = new CreateCustomerDTO(
                "bilal", "belhaj", email, "bilalbelhaj", "tetouan", "0678787878"
        );

        given(customerRepository.existsByEmail(email)).willReturn(true);
        // then
        assertThatThrownBy(() -> underTest.create(dto, user)).isInstanceOf(BadRequestException.class);
        verify(customerRepository, never()).save(any());
    }

    @Test
    @Disabled
    void getProfile() {
    }

    @Test
    @Disabled
    void updateProfile() {
    }

    @Test
    @Disabled
    void deleteAccount() {
    }
}