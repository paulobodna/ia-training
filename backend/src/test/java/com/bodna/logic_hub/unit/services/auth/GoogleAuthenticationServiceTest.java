package com.bodna.logic_hub.unit.services.auth;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.dtos.auth.LoginResponseDTO;
import com.bodna.logic_hub.exceptions.InvalidCredentialsException;
import com.bodna.logic_hub.infra.security.TokenService;
import com.bodna.logic_hub.repositories.UserRepository;
import com.bodna.logic_hub.services.auth.GoogleAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleAuthenticationServiceTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoogleAuthenticationService authenticationService;


    @Test
    void shouldRegisterUserWithGoogle() {
        OidcUser googleUser = mock(OidcUser.class);

        String userSubject = "testSubject123";
        String userEmail = "user@test.com";
        String userFullName = "Test User";

        when(googleUser.getSubject()).thenReturn(userSubject);
        when(googleUser.getEmail()).thenReturn(userEmail);
        when(googleUser.getFullName()).thenReturn(userFullName);

        when(userRepository.findByGoogleSubject(userSubject))
            .thenReturn(Optional.empty());

        when(userRepository.existsByEmail(userEmail))
            .thenReturn(false);

        User savedUser = User.createGoogleUser(
            userEmail,
            userFullName,
            userSubject
        );

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        when(tokenService.generateToken(savedUser))
            .thenReturn("jwt-token");

        LoginResponseDTO response = authenticationService.authenticateWithGoogle(googleUser);

        verify(userRepository).findByGoogleSubject(userSubject);
        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository).save(any(User.class));

        assertEquals("jwt-token", response.token());
    }

    @Test
    void shouldAuthenticateExistingUserWithGoogle() {
        OidcUser googleUser = mock(OidcUser.class);

        String userSubject = "testSubject123";
        String userEmail = "user@test.com";
        String userFullName = "Test User";

        when(googleUser.getSubject()).thenReturn(userSubject);
        when(googleUser.getEmail()).thenReturn(userEmail);

        User existingUser = User.createGoogleUser(
            userEmail,
            userFullName,
            userSubject
        );

        when(userRepository.findByGoogleSubject(userSubject))
            .thenReturn(Optional.of(existingUser));

        when(tokenService.generateToken(existingUser))
            .thenReturn("jwt-token");

        LoginResponseDTO response = authenticationService.authenticateWithGoogle(googleUser);

        verify(userRepository).findByGoogleSubject(userSubject);
        verify(userRepository, never()).existsByEmail(userEmail);
        verify(userRepository, never()).save(any(User.class));

        assertEquals("jwt-token", response.token());
    }

    @Test
    void shouldThrowExceptionWhenLocalUserAuthenticatesWithGoogle() {
        OidcUser googleUser = mock(OidcUser.class);

        String userSubject = "testSubject123";
        String userEmail = "user@test.com";

        when(googleUser.getSubject()).thenReturn(userSubject);
        when(googleUser.getEmail()).thenReturn(userEmail);

        when(userRepository.findByGoogleSubject(userSubject))
            .thenReturn(Optional.empty());
        when(userRepository.existsByEmail(userEmail))
            .thenReturn(true);

        assertThrows(
            InvalidCredentialsException.class,
            () -> authenticationService.authenticateWithGoogle(googleUser)
        );

        verify(userRepository).findByGoogleSubject(userSubject);
        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRegisterLongNamedUserWithGoogle() {
        OidcUser googleUser = mock(OidcUser.class);

        String userSubject = "testSubject123";
        String userEmail = "user@test.com";
        String userFullName = "a".repeat(25);

        when(googleUser.getSubject()).thenReturn(userSubject);
        when(googleUser.getEmail()).thenReturn(userEmail);
        when(googleUser.getFullName()).thenReturn(userFullName);

        when(userRepository.findByGoogleSubject(userSubject))
            .thenReturn(Optional.empty());

        when(userRepository.existsByEmail(userEmail))
            .thenReturn(false);

        User savedUser = User.createGoogleUser(
            userEmail,
            "testUsername",
            userSubject
        );

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        when(tokenService.generateToken(savedUser))
            .thenReturn("jwt-token");

        LoginResponseDTO response = authenticationService.authenticateWithGoogle(googleUser);

        verify(userRepository).findByGoogleSubject(userSubject);
        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository).existsByUsername(userFullName.substring(0, 20));
        verify(userRepository).save(any(User.class));

        assertEquals("jwt-token", response.token());
    }

    @Test
    void shouldRegisterExistingUsernameUserWithGoogle() {
        OidcUser googleUser = mock(OidcUser.class);

        String userSubject = "testSubject123";
        String userEmail = "user@test.com";
        String userFullName = "Test User";

        when(googleUser.getSubject()).thenReturn(userSubject);
        when(googleUser.getEmail()).thenReturn(userEmail);
        when(googleUser.getFullName()).thenReturn(userFullName);

        when(userRepository.findByGoogleSubject(userSubject))
            .thenReturn(Optional.empty());

        when(userRepository.existsByEmail(userEmail))
            .thenReturn(false);

        User savedUser = User.createGoogleUser(
            userEmail,
            "testUsername",
            userSubject
        );

        when(userRepository.existsByUsername("TestUser"))
            .thenReturn(true);
        when(userRepository.existsByUsername("TestUser1"))
            .thenReturn(false);

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        when(tokenService.generateToken(savedUser))
            .thenReturn("jwt-token");

        LoginResponseDTO response = authenticationService.authenticateWithGoogle(googleUser);

        verify(userRepository).findByGoogleSubject(userSubject);
        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository).existsByUsername("TestUser");
        verify(userRepository).existsByUsername("TestUser1");
        verify(userRepository).save(any(User.class));

        assertEquals("jwt-token", response.token());
    }
}