package com.bodna.logic_hub.integration.domain;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.integration.IntegrationTest;
import com.bodna.logic_hub.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
public class UserValidationTest extends IntegrationTest {

    @Autowired
    private UserRepository repository;


    @Test
    void shouldRejectNullUsername() {
        User user = User.createLocalUser(
            "ned@stark.com",
            "ned",
            "password"
        );

        user.setUsername(null);

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectBlankUsername() {
        User user = User.createLocalUser(
            "ned@stark.com",
            " ",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectUsernameShorterThan3Chars() {
        User user = User.createLocalUser(
            "ned@stark.com",
            "n",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectUsernameBiggerThan20Chars() {
        User user = User.createLocalUser(
            "ned@stark.com",
            "n".repeat(21),
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectNullEmail() {
        User user = User.createLocalUser(
            "ned@stark.com",
            "ned",
            "password"
        );

        user.setEmail(null);

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectBlankEmail() {
        User user = User.createLocalUser(
            " ",
            "ned",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectEmailShorterThan6Chars() {
        User user = User.createLocalUser(
            "a@t.b",
            "ned",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectEmailBiggerThan20Chars() {
        User user = User.createLocalUser(
            "a".repeat(254) + "@test.com",
            "ned",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectInvalidEmail() {
        User user = User.createLocalUser(
            "notAValidEmail",
            "ned",
            "password"
        );

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }

    @Test
    void shouldRejectNullRole() {
        User user = User.createLocalUser(
            "ned@stark.com",
            "ned",
            "password"
        );

        user.setRole(null);

        assertThrows(
            ConstraintViolationException.class,
            () -> repository.saveAndFlush(user)
        );
    }
}
