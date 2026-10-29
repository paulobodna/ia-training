package com.bodna.logic_hub.infra.security;

import com.bodna.logic_hub.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class AuthenticatedUserProvider {

    public static Long getUserId() {
        return getUser().getId();
    }

    private static User getUser() {
        return (User) Objects.requireNonNull(
            SecurityContextHolder
                .getContext()
                .getAuthentication()
        ).getPrincipal();
    }
}
