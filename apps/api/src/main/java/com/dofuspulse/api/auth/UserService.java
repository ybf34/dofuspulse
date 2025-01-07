package com.dofuspulse.api.auth;

import com.dofuspulse.api.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserPrincipal> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(UserPrincipal user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        userRepository.save(user);
    }
}
