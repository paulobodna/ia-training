package com.bodna.logic_hub.infra.security;

import com.bodna.logic_hub.services.auth.GoogleAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final GoogleAuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(
        @NonNull HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        assert oidcUser != null;
        String token = authenticationService
            .authenticateWithGoogle(oidcUser)
            .token();

        response.sendRedirect(
            "http://localhost:4200/oauth2/success?token=" + token
        );
    }
}
