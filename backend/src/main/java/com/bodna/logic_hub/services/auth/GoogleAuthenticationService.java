package com.bodna.logic_hub.services.auth;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.dtos.auth.LoginResponseDTO;
import com.bodna.logic_hub.exceptions.InvalidCredentialsException;
import com.bodna.logic_hub.infra.security.TokenService;
import com.bodna.logic_hub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
public class GoogleAuthenticationService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    public LoginResponseDTO authenticateWithGoogle(OidcUser googleUser) {
        String subject = googleUser.getSubject();
        String email = googleUser.getEmail();

        Optional<User> existingGoogleUser = userRepository.findByGoogleSubject(subject);

        if (existingGoogleUser.isPresent()) {
            return generateTokenAndLoginResponse(existingGoogleUser.get());
        }

        if (userRepository.existsByEmail(email)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return generateTokenAndLoginResponse(createGoogleUser(googleUser));
    }

    private LoginResponseDTO generateTokenAndLoginResponse(User user) {
        return new LoginResponseDTO(tokenService.generateToken(user));
    }

    private User createGoogleUser(OidcUser googleUser) {
        String email = googleUser.getEmail();
        String googleSubject = googleUser.getSubject();
        String username = generateUniqueUsername(
            Objects.requireNonNullElse(googleUser.getFullName(), "anonyBear")
        );

        User user = User.createGoogleUser(email, username, googleSubject);

        return userRepository.save(user);
    }

    private String generateUniqueUsername(String name) {
        String base = name.replaceAll("[^a-zA-Z0-9]", "");

        if (base.length() > 20) {
            base = base.substring(0, 20);
        }

        String username = base;
        int suffix = 1;

        while (userRepository.existsByUsername(username)) {

            String suffixString = String.valueOf(suffix++);

            int maxBaseLength = 20 - suffixString.length();

            String shortenedBase = base.substring(
                0,
                Math.min(base.length(), maxBaseLength)
            );

            username = shortenedBase + suffixString;
        }

        return username;
    }
}
