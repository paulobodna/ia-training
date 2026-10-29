package com.bodna.logic_hub.unit.services;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.exceptions.UserNotFoundException;
import com.bodna.logic_hub.infra.security.AuthenticatedUserProvider;
import com.bodna.logic_hub.repositories.UserRepository;
import com.bodna.logic_hub.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetCurrentUser() {
        Long userId = 1L;

        User user = User.createLocalUser(
            "teste@gmail.com",
            "testUser",
            "encryptedPassword"
        );

        when(userRepository.findById(userId))
            .thenReturn(Optional.of(user));

        try (MockedStatic<AuthenticatedUserProvider> mockedProvider =
                 mockStatic(AuthenticatedUserProvider.class)) {

            mockedProvider.when(AuthenticatedUserProvider::getUserId)
                .thenReturn(userId);

            User result = userService.getCurrentUser();

            assertEquals(user, result);

            verify(userRepository).findById(userId);
        }
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId))
            .thenReturn(Optional.empty());

        try (MockedStatic<AuthenticatedUserProvider> mockedProvider =
                 mockStatic(AuthenticatedUserProvider.class)) {

            mockedProvider.when(AuthenticatedUserProvider::getUserId)
                .thenReturn(userId);

            assertThrows(
                UserNotFoundException.class,
                () -> userService.getCurrentUser()
            );

            verify(userRepository).findById(userId);
        }
    }
}