package com.dofuspulse.api.auth.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserService;
import com.dofuspulse.api.auth.oauth2.OAuth2UserInfo;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import com.dofuspulse.api.auth.oauth2.UserSocialLoginService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("User Social Service Unit Tests")
@ExtendWith(MockitoExtension.class)
class UserSocialLoginServiceUnitTest {

  private static final String TEST_EMAIL = "test@example.com";
  private static final String TEST_PROVIDER = "google";
  private static final String TEST_PROVIDER_ID = "12345";

  @Mock
  UserService userService;

  @InjectMocks
  UserSocialLoginService userSocialLoginService;

  @Test
  @DisplayName("should return existing user when social login already registered")
  void shouldReturnExistingUserWhenSocialLoginAlreadyRegistered() {
    String email = "test@example.com";

    UserPrincipal existingUser = new UserPrincipal();
    existingUser.setEmail(email);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider(TEST_PROVIDER);
    userSocial.setProviderId(TEST_PROVIDER_ID);
    existingUser.setSocialLogins(List.of(userSocial));

    when(userService.findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString())).thenReturn(Optional.of(existingUser));

    OAuth2UserInfo userOauth2Info = createOAuth2UserInfo();

    UserPrincipal user = userSocialLoginService.linkOrRegisterSocialAccount(userOauth2Info);

    assertNotNull(user);
    assertThat(user).isEqualTo(existingUser);

    verify(userService, times(0)).saveUser(any(UserPrincipal.class));
    verify(userService, times(0)).findByEmail(anyString());
    verify(userService, times(1)).findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString());
  }


  @Test
  @DisplayName("Should create new user account link to the social login")
  void shouldCreateNewAccountAndLinkSocialLogin() {
    //given oauth2 user identity
    OAuth2UserInfo userOauth2Info = createOAuth2UserInfo();

    //when a new user is registering through oauth2
    when(userService.findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString())).thenReturn(Optional.empty());
    when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
    when(userService.saveUser(any(UserPrincipal.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    UserPrincipal newUser = userSocialLoginService.linkOrRegisterSocialAccount(userOauth2Info);

    assertThat(newUser)
        .isNotNull()
        .satisfies(user -> {
          assertThat(user.getEmail()).isEqualTo(userOauth2Info.getEmail());
          assertThat(user.getRole()).isEqualTo(Role.USER);
          assertThat(user.getSocialLogins())
              .hasSize(1)
              .first()
              .extracting(UserSocialLogin::getProvider, UserSocialLogin::getProviderId)
              .contains(userOauth2Info.getProvider(), userOauth2Info.getProviderId());
        });

    verify(userService, times(1)).saveUser(any(UserPrincipal.class));
    verify(userService, times(1)).findByEmail(anyString());
    verify(userService, times(1)).findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString());
  }

  @Test
  @DisplayName("Should link the new social login to the existing user account")
  void shouldAddSocialLoginToExistingAccount() {
    String email = "test@example.com";
    UserPrincipal existingUser = new UserPrincipal();
    existingUser.setEmail(email);

    OAuth2UserInfo userOauth2Info = createOAuth2UserInfo();

    when(userService.findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString())).thenReturn(Optional.empty());
    when(userService.findByEmail(userOauth2Info.getEmail())).thenReturn(Optional.of(existingUser));
    when(userService.saveUser(any(UserPrincipal.class))).thenAnswer(
        invocationOnMock -> invocationOnMock.getArgument(0));

    UserPrincipal userWithAddedSocialLogin = userSocialLoginService.linkOrRegisterSocialAccount(
        userOauth2Info);

    assertThat(userWithAddedSocialLogin)
        .isNotNull()
        .satisfies(user -> {
          assertThat(user.getEmail()).isEqualTo(email);
          assertThat(user.getSocialLogins())
              .hasSize(1).first()
              .extracting(UserSocialLogin::getProvider, UserSocialLogin::getProviderId)
              .contains(userOauth2Info.getProvider(), userOauth2Info.getProviderId());
        });

    verify(userService, times(1)).saveUser(any(UserPrincipal.class));
    verify(userService, times(1)).findByEmail(anyString());
    verify(userService, times(1)).findBySocialLoginsProviderAndSocialLoginsProviderId(anyString(),
        anyString());
  }

  OAuth2UserInfo createOAuth2UserInfo() {
    OAuth2UserInfo userInfo = new OAuth2UserInfo();
    userInfo.setEmail(TEST_EMAIL);
    userInfo.setProvider(TEST_PROVIDER);
    userInfo.setProviderId(TEST_PROVIDER_ID);
    return userInfo;
  }
}
