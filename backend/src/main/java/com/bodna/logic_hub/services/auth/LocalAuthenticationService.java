package com.bodna.logic_hub.services.auth;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.dtos.auth.LoginRequestDTO;
import com.bodna.logic_hub.dtos.auth.LoginResponseDTO;
import com.bodna.logic_hub.dtos.auth.SignUpDTO;
import com.bodna.logic_hub.exceptions.ConflictedEmailException;
import com.bodna.logic_hub.exceptions.ConflictedUsernameException;
import com.bodna.logic_hub.exceptions.InvalidCredentialsException;
import com.bodna.logic_hub.infra.security.TokenService;
import com.bodna.logic_hub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LocalAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticateUser(LoginRequestDTO request) {
        Optional<User> user = userRepository.findByEmail(request.login());

        if (user.isEmpty() || !user.get().isLocalUser()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UsernamePasswordAuthenticationToken userPassword =
            new UsernamePasswordAuthenticationToken(request.login(), request.password());

        Authentication auth = authenticationManager.authenticate(userPassword);

        return generateTokenAndLoginResponse((User) auth.getPrincipal());
    }

    public void registerUser(SignUpDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictedEmailException("Email is already in use");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictedUsernameException("Username is already in use");
        }

        String encryptedPassword = passwordEncoder.encode(request.password());
        User newUser = User.createLocalUser(request.email(), request.username(), encryptedPassword);

        userRepository.save(newUser);
    }

    private LoginResponseDTO generateTokenAndLoginResponse(User user) {
        return new LoginResponseDTO(tokenService.generateToken(user));
    }
}
