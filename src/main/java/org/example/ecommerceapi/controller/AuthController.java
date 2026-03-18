package org.example.ecommerceapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.entity.Role;
import org.example.ecommerceapi.entity.User;
import org.example.ecommerceapi.repository.RoleRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody Map<String,String> body){

        User user = new User();
        user.setEmail(body.get("email"));
        user.setPassword(passwordEncoder.encode(body.get("password")));

        Role role =
                roleRepository.findByName("ROLE_CUSTOMER").orElseThrow();

        user.getRoles().add(role);
        userRepository.save(user);

        return "Customer registered";
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> body){

        User user =
                userRepository.findByEmail(body.get("email")).orElseThrow();

        if(!passwordEncoder.matches(body.get("password"),
                user.getPassword()))
            throw new RuntimeException("Bad credentials");

        String token = jwtService.generateToken(user);

        return Map.of("token",token);
    }
}
