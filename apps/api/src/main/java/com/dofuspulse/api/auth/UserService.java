package com.dofuspulse.api.auth;

import com.dofuspulse.api.exception.UserAlreadyExistsException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Optional<UserPrincipal> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Long saveUser(UserPrincipal user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException(
          "User with email " + user.getEmail() + " already exists");
    }
    userRepository.save(user);
    return user.getId();
  }
}
