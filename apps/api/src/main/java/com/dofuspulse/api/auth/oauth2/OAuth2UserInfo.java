package com.dofuspulse.api.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserInfo {

  String provider;
  String providerId;
  String email;
}
