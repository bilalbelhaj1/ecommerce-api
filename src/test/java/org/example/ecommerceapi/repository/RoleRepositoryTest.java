package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Role;
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
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;
    private final String name = "ADMIN_ROLE";

    @Test
    void findByName_returnsRole_whenExists() {
        // given
        underTest.save(Role.builder().name(name).build());
        // when
        Optional<Role> res = underTest.findByName(name);
        // then
        assertThat(res).isNotEmpty();
    }
    @Test
    void findByName_returnsEmpty_whenNotExists() {
        // when
        Optional<Role> res = underTest.findByName(name);
        // then
        assertThat(res).isEmpty();
    }
}