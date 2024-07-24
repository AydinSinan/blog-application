package com.microservice.userservice.service;

import com.microservice.userservice.dao.request.SignUpRequest;
import com.microservice.userservice.dao.request.SigninRequest;
import com.microservice.userservice.dao.response.JwtAuthenticationResponse;
import com.microservice.userservice.model.User;
import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(savedUser); // getUsername
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public JwtAuthenticationResponse login(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        String jwt = jwtService.generateToken(user); // getUsername
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public void logout() {
        System.out.println("Logged out");
    }
}
