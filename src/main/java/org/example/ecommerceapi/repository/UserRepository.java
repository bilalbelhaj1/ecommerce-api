package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author $(bilal belhaj)
 **/
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
