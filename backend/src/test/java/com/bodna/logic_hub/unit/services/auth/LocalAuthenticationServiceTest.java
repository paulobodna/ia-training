package com.bodna.logic_hub.unit.services.auth;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.dtos.auth.LoginRequestDTO;
import com.bodna.logic_hub.dtos.auth.LoginResponseDTO;
import com.bodna.logic_hub.dtos.auth.SignUpDTO;
import com.bodna.logic_hub.exceptions.ConflictedEmailException;
import com.bodna.logic_hub.exceptions.ConflictedUsernameException;
import com.bodna.logic_hub.exceptions.InvalidCredentialsException;
import com.bodna.logic_hub.infra.security.TokenService;
import com.bodna.logic_hub.repositories.UserRepository;
import com.bodna.logic_hub.services.auth.LocalAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalAuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private LocalAuthenticationService authenticationService;

    @Test
    void shouldRegisterNewLocalUser() {
        SignUpDTO request = new SignUpDTO(
            "teste@gmail.com",
            "testUser",
            "testPassword"
        );

        when(userRepository.existsByEmail(eq(request.email())))
            .thenReturn(false);
        when(userRepository.existsByUsername(eq(request.username())))
            .thenReturn(false);
        when(encoder.encode(request.password()))
            .thenReturn("encryptedPassword");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        authenticationService.registerUser(request);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(request.email(), savedUser.getEmail());
        assertEquals(request.username(), savedUser.getUsername());
        assertEquals("encryptedPassword", savedUser.getPassword());
    }

    @Test
    void shouldThrowConflictedEmailExceptionWhenEmailIsAlreadyInUse() {
        SignUpDTO request = new SignUpDTO(
            "teste@gmail.com",
            "testUser",
            "testPassword"
        );

        when(userRepository.existsByEmail(eq(request.email())))
            .thenReturn(true);

        assertThrows(
            ConflictedEmailException.class,
            () -> authenticationService.registerUser(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowConflictedUsernameExceptionWhenEmailAlreadyExists() {
        SignUpDTO request = new SignUpDTO(
            "teste@gmail.com",
            "testUser",
            "testPassword"
        );

        when(userRepository.existsByEmail(eq(request.email())))
            .thenReturn(false);
        when(userRepository.existsByUsername(eq(request.username())))
            .thenReturn(true);

        assertThrows(
            ConflictedUsernameException.class,
            () -> authenticationService.registerUser(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldAuthenticateUserLocally() {
        LoginRequestDTO request = new LoginRequestDTO(
            "teste@gmail.com",
            "testPassword"
        );

        User user = User.createLocalUser(
            "teste@gmail.com",
            "testUser",
            "encryptedPassword"
        );

        when(userRepository.findByEmail(request.login()))
            .thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(
            UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(user);

        when(tokenService.generateToken(user))
            .thenReturn("jwt-token");

        LoginResponseDTO response =
            authenticationService.authenticateUser(request);

        assertEquals("jwt-token", response.token());

        verify(authenticationManager).authenticate(
            argThat(token ->
                Objects.equals(token.getPrincipal(), request.login())
                    && Objects.equals(token.getCredentials(), request.password())
            )
        );

        verify(tokenService).generateToken(user);
    }

    @Test
    void shouldFailAuthenticationWithInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO(
            "teste@gmail.com",
            "wrongPassword"
        );

        User user = User.createLocalUser(
            "teste@gmail.com",
            "testUser",
            "encryptedPassword"
        );

        when(userRepository.findByEmail(request.login()))
            .thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any(
            UsernamePasswordAuthenticationToken.class
        ))).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(
            BadCredentialsException.class,
            () -> authenticationService.authenticateUser(request)
        );

        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenNoUserWithInformedEmail() {
        LoginRequestDTO request = new LoginRequestDTO(
            "teste@gmail.com",
            "anyPassword"
        );

        when(userRepository.findByEmail(request.login()))
            .thenReturn(Optional.empty());

        assertThrows(
            InvalidCredentialsException.class,
            () -> authenticationService.authenticateUser(request)
        );

        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserIsNotLocallyRegistered() {
        LoginRequestDTO request = new LoginRequestDTO(
            "teste@gmail.com",
            "anyPassword"
        );

        User user = User.createGoogleUser(
            "teste@gmail.com",
            "testUser",
            "ABC12345DEF"
        );

        when(userRepository.findByEmail(request.login()))
            .thenReturn(Optional.of(user));

        assertThrows(
            InvalidCredentialsException.class,
            () -> authenticationService.authenticateUser(request)
        );

        verify(tokenService, never()).generateToken(any());
    }
}