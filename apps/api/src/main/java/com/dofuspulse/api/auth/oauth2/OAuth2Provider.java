package com.dofuspulse.api.auth.oauth2;

import java.util.Map;


public enum OAuth2Provider {
  GOOGLE("google", "sub"),
  DISCORD("discord", "id");

  private final String name;
  private final String idKey;

  OAuth2Provider(String name, String providerIdKey) {
    this.name = name;
    this.idKey = providerIdKey;
  }

  public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
    String providerId = (String) attributes.get(idKey);
    String email = (String) attributes.get("email");
    return new OAuth2UserInfo(name, providerId, email);
  }
}
