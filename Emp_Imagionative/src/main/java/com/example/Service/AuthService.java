package com.example.service;

import com.example.dto.AuthRequestDto;
import com.example.dto.AuthResponseDto;
import com.example.exception.AuthenticationException;
import com.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDto authenticate(AuthRequestDto authRequest) {
        // Validate user credentials (this can be adapted based on your user management logic)
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        if (userDetails == null || !passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        String jwt = jwtUtil.generateToken(userDetails);
        return new AuthResponseDto(jwt);
    }
}
