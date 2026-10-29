package com.bodna.logic_hub.integration.repositories;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.domain.user.UserRole;
import com.bodna.logic_hub.integration.IntegrationTest;
import com.bodna.logic_hub.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class UserRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldCreateLocalUser() {
        String email = "ned@stark.com";
        String username = "ned";
        String password = "password";

        User user = User.createLocalUser(
            email,
            username,
            password
        );

        assertDoesNotThrow(() -> {
            User savedUser = repository.saveAndFlush(user);

            assertNotNull(savedUser.getId());
            assertEquals(email, savedUser.getEmail());
            assertEquals(username, savedUser.getUsername());
            assertEquals(password, savedUser.getPassword());
            assertNull(savedUser.getGoogleSubject());
            assertEquals(UserRole.USER, savedUser.getRole());
            assertEquals(0, savedUser.getExperience());
        });
    }

    @Test
    void shouldCreateGoogleUser() {
        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        assertDoesNotThrow(() -> {
            User savedUser = repository.saveAndFlush(user);

            assertNotNull(savedUser.getId());
            assertEquals(email, savedUser.getEmail());
            assertEquals(username, savedUser.getUsername());
            assertNull(savedUser.getPassword());
            assertEquals(subject, savedUser.getGoogleSubject());
            assertEquals(UserRole.USER, savedUser.getRole());
            assertEquals(0, savedUser.getExperience());
        });
    }

    @Test
    void shouldFindUserByEmail() {

        String email = "ned@stark.com";
        String username = "ned";
        String password = "password";

        User user = User.createLocalUser(
            email,
            username,
            password
        );

        repository.saveAndFlush(user);

        assertTrue(repository.existsByEmail(email));

        Optional<User> result = repository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        assertEquals(password, result.get().getPassword());
    }

    @Test
    void shouldNotFindUserByEmail() {

        User user = User.createLocalUser(
            "ned@stark.com",
            "ned",
            "password"
        );

        repository.saveAndFlush(user);

        assertFalse(repository.existsByEmail("notfound@email.com"));

        Optional<User> result = repository.findByEmail("notfound@email.com");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindUserBySubject() {

        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        repository.saveAndFlush(user);

        Optional<User> result = repository.findByGoogleSubject(subject);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals("ned", result.get().getUsername());
    }

    @Test
    void shouldNotFindUserBySubject() {

        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        repository.saveAndFlush(user);

        Optional<User> result = repository.findByGoogleSubject("NotFoundSubject123");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindUserByUsername() {

        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        repository.saveAndFlush(user);

        assertTrue(repository.existsByUsername(username));
    }

    @Test
    void shouldNotFindUserByUsername() {

        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        repository.saveAndFlush(user);

        assertFalse(repository.existsByUsername("sansa12"));
    }

    @Test
    void shouldRejectDuplicatedEmail() {

        String email = "ned@stark.com";
        String username = "ned";
        String password = "password";

        User user = User.createLocalUser(
            email,
            username,
            password
        );

        repository.saveAndFlush(user);

        assertTrue(repository.existsByEmail(email));

        User duplicatedEmailUser = User.createLocalUser(
            email,
            "differentUsername",
            "differentPassword"
        );

        assertThrows(
            DataIntegrityViolationException.class,
            () -> repository.saveAndFlush(duplicatedEmailUser)
        );
    }

    @Test
    void shouldRejectDuplicatedUsername() {

        String email = "ned@stark.com";
        String username = "ned";
        String password = "password";

        User user = User.createLocalUser(
            email,
            username,
            password
        );

        repository.saveAndFlush(user);

        assertTrue(repository.existsByUsername(username));

        User duplicatedUsernameUser = User.createLocalUser(
            "different@email.com",
            username,
            "differentPassword"
        );

        assertThrows(
            DataIntegrityViolationException.class,
            () -> repository.saveAndFlush(duplicatedUsernameUser)
        );
    }

    @Test
    void shouldRejectDuplicatedGoogleSubject() {

        String email = "ned@stark.com";
        String username = "ned";
        String subject = "GoogleSubject123";

        User user = User.createGoogleUser(
            email,
            username,
            subject
        );

        repository.saveAndFlush(user);

        assertTrue(repository.findByGoogleSubject(subject).isPresent());

        User duplicatedSubjectUser = User.createGoogleUser(
            "different@email.com",
            "differentUsername",
            subject
        );

        assertThrows(
            DataIntegrityViolationException.class,
            () -> repository.saveAndFlush(duplicatedSubjectUser)
        );
    }
}
