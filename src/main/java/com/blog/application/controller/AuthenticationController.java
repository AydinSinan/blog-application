package com.blog.application.controller;

import com.blog.application.dao.request.SignUpRequest;
import com.blog.application.dao.request.SigninRequest;
import com.blog.application.dao.response.JwtAuthenticationResponse;
import com.blog.application.model.User;
import com.blog.application.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("/user-details")
    public String getUserDetails(@AuthenticationPrincipal User userDetails) {
        Integer userId = userDetails.getId();
        String username = userDetails.getUsername();

        return "User ID: " + userId + ", Username: " + username;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authenticationService.logout();
        return ResponseEntity.ok().build();
    }
}
