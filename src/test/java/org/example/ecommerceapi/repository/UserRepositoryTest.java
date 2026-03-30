package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private final String username = "bilal@gmail.com";

    @Test
    void findByUsername_returnUser_WhenExists() {
        // given
        userRepository.save(
                AppUser.builder().username(username).password("password").build()
        );
        // when
        Optional<AppUser> res = userRepository.findByUsername(username);
        // then
        assertThat(res).isNotEmpty();
    }

    @Test
    void findByUsername_returnEmpty_WhenNotExists() {
        Optional<AppUser> res = userRepository.findByUsername(username);
        // then
        assertThat(res).isEmpty();
    }

    @Test
    void existsByUsername_returnTrue_whenExists() {
        // given
        userRepository.save(
                AppUser.builder().username(username).password("password").build()
        );
        // when
        boolean res = userRepository.existsByUsername(username);
        // then
        assertThat(res).isTrue();
    }

    @Test
    void existsByUsername_returnFalse_whenNotExists() {
        // when
        boolean res = userRepository.existsByUsername(username);
        // then
        assertThat(res).isFalse();
    }
}