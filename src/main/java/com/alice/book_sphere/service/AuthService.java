package com.alice.book_sphere.service;

import com.alice.book_sphere.dto.JwtRequest;
import com.alice.book_sphere.dto.JwtResponse;
import com.alice.book_sphere.dto.RegistrationUserDto;
import com.alice.book_sphere.http.handler.ErrorResponse;
import com.alice.book_sphere.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest jwtRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateJwtToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(
                    token,
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()),
                    jwtUtils.getJwtExpirationMs() / 1000
            ));
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for email: {}", jwtRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        } catch (Exception e) {
            log.error("Internal error during authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationUserDto registrationDto) {
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Email already registered"));
        }

        userService.createNewUser(
                registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getPassword()
        );

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
