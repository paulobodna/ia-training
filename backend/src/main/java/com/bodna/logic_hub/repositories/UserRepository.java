package com.bodna.logic_hub.repositories;

import com.bodna.logic_hub.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleSubject(String subject);

    boolean existsByEmail(String email);

    boolean existsByUsername(String email);
}