package com.bodna.logic_hub.controllers;

import com.bodna.logic_hub.dtos.auth.LoginRequestDTO;
import com.bodna.logic_hub.dtos.auth.SignUpDTO;
import com.bodna.logic_hub.services.auth.LocalAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private LocalAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.authenticateUser(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpDTO request) {
        this.authenticationService.registerUser(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("User created successfully");
    }
}
