package com.dofuspulse.api.user.dto;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.oauth2.UserSocialLoginDto;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record UserProfileDto(
    Long id,
    String email,
    Role role,
    List<UserSocialLoginDto> socials,
    Map<String, Object> attributes,
    Instant createdAt,
    Instant updatedAt
) {

  public UserProfileDto(UserPrincipal user) {
    this(
        user.getId(),
        user.getEmail(),
        user.getRole(),
        user.getSocialLogins().stream()
            .map(UserSocialLoginDto::new)
            .toList(),
        user.getAttributes(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
  }
}
