package com.dofuspulse.api.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public Optional<UserPrincipal> findBySocialLoginsProviderAndSocialLoginsProviderId(
      String provider,
      String providerId) {
    return userRepository.findBySocialLoginsProviderAndSocialLoginsProviderId(provider, providerId);
  }

  public Optional<UserPrincipal> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public UserPrincipal saveUser(UserPrincipal user) {
    return userRepository.save(user);
  }
}
