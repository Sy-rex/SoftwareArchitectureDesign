package com.sobolev.spring.springlab1.controller;

import com.sobolev.spring.springlab1.dto.JwtRequest;
import com.sobolev.spring.springlab1.dto.JwtResponse;
import com.sobolev.spring.springlab1.dto.RegistrationUserDTO;
import com.sobolev.spring.springlab1.entity.User;
import com.sobolev.spring.springlab1.security.JwtTokenUtils;
import com.sobolev.spring.springlab1.service.RegistrationService;
import com.sobolev.spring.springlab1.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailService userDetailService;
    private final JwtTokenUtils jwtTokenUtils;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationUserDTO userDTO) {
        User user = convertToUser(userDTO);

        registrationService.register(user);

        UserDetails userDetails = userDetailService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Неправильный логин или пароль"));
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private User convertToUser(RegistrationUserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
