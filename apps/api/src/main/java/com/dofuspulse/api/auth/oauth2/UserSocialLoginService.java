package com.dofuspulse.api.auth.oauth2;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSocialLoginService {

  private final UserService userService;

  @Transactional
  public UserPrincipal linkOrRegisterSocialAccount(OAuth2UserInfo userInfo) {
    var existingUser = userService.findBySocialLoginsProviderAndSocialLoginsProviderId(
        userInfo.getProvider(), userInfo.getProviderId());

    return existingUser.orElseGet(() -> userService.findByEmail(userInfo.getEmail())
        .map(user -> addSocialLoginToExistingUser(user, userInfo))
        .orElseGet(() -> createNewUserWithSocialLogin(userInfo)));
  }

  private UserPrincipal addSocialLoginToExistingUser(
      UserPrincipal existingUser,
      OAuth2UserInfo userInfo) {
    UserSocialLogin socialLogin = createSocialLogin(existingUser, userInfo);
    existingUser.getSocialLogins().add(socialLogin);
    return userService.saveUser(existingUser);
  }


  private UserPrincipal createNewUserWithSocialLogin(OAuth2UserInfo userInfo) {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setEmail(userInfo.getEmail());
    newUser.setPassword(null);
    newUser.setRole(Role.USER);

    UserSocialLogin socialLogin = createSocialLogin(newUser, userInfo);
    newUser.getSocialLogins().add(socialLogin);

    return userService.saveUser(newUser);
  }

  private UserSocialLogin createSocialLogin(UserPrincipal user, OAuth2UserInfo userInfo) {
    UserSocialLogin socialLogin = new UserSocialLogin();
    socialLogin.setProvider(userInfo.getProvider());
    socialLogin.setProviderId(userInfo.getProviderId());
    socialLogin.setUser(user);
    return socialLogin;
  }

}
