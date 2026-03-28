package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindCustomerByEmail() {
    }

    @Test
    void itShouldCheckCustomerExistsByEmail() {
        // given
        String email = "bilal@gmail.com";
        AppUser user = AppUser.builder()
                .username(email)
                .password(passwordEncoder.encode("bilalbelhaj"))
                .build();

        userRepository.save(user);
        Customer customer = Customer.builder()
                .email(email)
                .firstName("bilal")
                .lastName("belhaj")
                .address("tetouan")
                .appUser(user)
                .build();

        underTest.save(customer);
        // when
        boolean exists = underTest.existsByEmail(email);
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckCustomerEmailDoesNotExists() {
        // given
        String email = "bila@gmail.com";
        // when
        boolean exists = underTest.existsByEmail(email);
        // then
        assertThat(exists).isFalse();
    }
}