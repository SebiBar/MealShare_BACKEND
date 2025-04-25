package com.example.memanager.security;

import com.example.memanager.dto.LoginRequest;
import com.example.memanager.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.memanager.user.User;
import com.example.memanager.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", userDetails.getAuthorities());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

}