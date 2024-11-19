package com.telemis.exercice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telemis.exercice.dto.LoginRequest;
import com.telemis.exercice.services.AuthService;
import com.telemis.exercice.services.CustomUserDetailsService;
import com.telemis.exercice.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
        authService.register(request);
        authService.login(request);
        
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        authService.login(request);

        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(token);
    }
}
