package com.bodna.logic_hub.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false)
    private String username;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true)
    private String googleSubject;

    @NotNull
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private int experience;

    public static User createLocalUser(
        String email,
        String username,
        String password
    ) {
        User user = new User();

        user.email = email;
        user.username = username;
        user.password = password;
        user.role = UserRole.USER;
        user.experience = 0;

        return user;
    }

    public static User createGoogleUser(
        String email,
        String username,
        String googleSubject
    ) {
        User user = new User();

        user.email = email;
        user.username = username;
        user.googleSubject = googleSubject;
        user.role = UserRole.USER;
        user.experience = 0;

        return user;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public boolean isLocalUser() {
        return this.password != null;
    }
}
