package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
