package com.dofuspulse.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userService.findByEmail(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("User with email " + username + " not found"));
  }
}
