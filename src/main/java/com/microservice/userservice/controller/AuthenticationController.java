package com.microservice.userservice.controller;

import com.microservice.userservice.dao.request.SignUpRequest;
import com.microservice.userservice.dao.request.SigninRequest;
import com.microservice.userservice.dao.response.JwtAuthenticationResponse;
import com.microservice.userservice.model.User;
import com.microservice.userservice.service.AuthenticationService;
import com.microservice.userservice.service.JwtService;
import com.microservice.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/find-by-email")
    public String getUserEmail(@AuthenticationPrincipal User userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("UserDetails in /find-by-email is null");

        }
        return userDetails.getEmail();
    }

    @GetMapping("/user-details")
    public String getUserDetails(@AuthenticationPrincipal User userDetails) {
        Integer userId = userDetails.getId();
        String username = userDetails.getUsername();
        String email = userDetails.getEmail();

        return "User ID: " + userId + ", Username: " + username + ", Email: " + email;
    }

    @GetMapping("/validate-token")
    public ResponseEntity<UserDetails> validateToken(@RequestParam("token") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String username = jwtService.extractUsername(token);;
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authenticationService.logout();
        return ResponseEntity.ok().build();
    }
}
