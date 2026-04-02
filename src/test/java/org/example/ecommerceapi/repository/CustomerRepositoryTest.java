package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author $(bilal belhaj)
 **/

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private UserRepository userRepository;

    private AppUser user;
    private Customer customer;

    private final String email = "bilal@gmail.com";

    @BeforeEach
    void setUp() {
        user = AppUser.builder()
                .username(email)
                .password("bilalbelhaj")
                .build();

        customer = Customer.builder()
                .email(email)
                .firstName("bilal")
                .lastName("belhaj")
                .address("tetouan")
                .build();
    }

    @Test
    void findByEmail_shouldReturnCustomer_whenEmailExists() {
        AppUser savedUser = userRepository.save(user);
        customer.setAppUser(savedUser);
        underTest.save(customer);

        Optional<Customer> result = underTest.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        assertThat(underTest.findByEmail(email)).isEmpty();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        AppUser savedUser = userRepository.save(user);
        customer.setAppUser(savedUser);
        underTest.save(customer);

        boolean result = underTest.existsByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        boolean result = underTest.existsByEmail(email);

        assertThat(result).isFalse();
    }
}