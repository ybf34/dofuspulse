package com.dofuspulse.api.auth.oauth2;

import com.dofuspulse.api.auth.UserPrincipal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserSocialLoginService socialLoginService;
  private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate
      = new DefaultOAuth2UserService();

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    OAuth2UserInfo oAuth2UserInfo = extractUserInfo(userRequest, oAuth2User);
    UserPrincipal user = socialLoginService.linkOrRegisterSocialAccount(oAuth2UserInfo);
    user.setAttributes(oAuth2User.getAttributes());
    return user;
  }

  private OAuth2UserInfo extractUserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = oAuth2User.getAttributes();
    return OAuth2Provider.valueOf(registrationId.toUpperCase()).extractUserInfo(attributes);
  }
}
