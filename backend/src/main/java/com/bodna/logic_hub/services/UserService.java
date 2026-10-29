package com.bodna.logic_hub.services;

import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.exceptions.UserNotFoundException;
import com.bodna.logic_hub.infra.security.AuthenticatedUserProvider;
import com.bodna.logic_hub.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User getCurrentUser() {
        Long userId = AuthenticatedUserProvider.getUserId();

        Optional<User> user = repository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with Id " + userId + " not found");
        }

        return user.get();
    }
}
