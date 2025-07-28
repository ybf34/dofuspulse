package com.dofuspulse.api.auth.oauth2;

public record UserSocialLoginDto(String provider, String providerId) {

  public UserSocialLoginDto(UserSocialLogin userSocialEntity) {
    this(userSocialEntity.getProvider(), userSocialEntity.getProviderId());
  }
}
