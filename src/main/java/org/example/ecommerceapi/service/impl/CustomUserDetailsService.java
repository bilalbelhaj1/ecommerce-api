package org.example.ecommerceapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author $(bilal belhaj)
 **/

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));

        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getName()))
                        .toList()
        );
    }
}