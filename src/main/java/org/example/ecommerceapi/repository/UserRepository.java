package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author $(bilal belhaj)
 **/
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
