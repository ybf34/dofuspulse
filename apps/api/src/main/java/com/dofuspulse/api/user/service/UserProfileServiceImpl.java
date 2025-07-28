package com.dofuspulse.api.user.service;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.dofuspulse.api.user.service.contract.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

  private final UserRepository userRepository;

  @Transactional
  public UserProfileDto getUserProfile(UserPrincipal userPrincipal) {

    UserPrincipal user = userRepository.findById(userPrincipal.getId())
        .orElseThrow(() ->
            new UsernameNotFoundException("User with id " + userPrincipal.getId() + " not found"));

    user.setAttributes(userPrincipal.getAttributes());
    return new UserProfileDto(user);
  }
}
