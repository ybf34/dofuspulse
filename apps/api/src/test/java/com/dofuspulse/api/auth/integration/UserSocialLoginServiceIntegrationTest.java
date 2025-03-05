package com.dofuspulse.api.auth.integration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.UserService;
import com.dofuspulse.api.auth.oauth2.OAuth2UserInfo;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import com.dofuspulse.api.auth.oauth2.UserSocialLoginService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("User social login integration test")
@DataJpaTest
public class UserSocialLoginServiceIntegrationTest extends PostgresIntegrationTestContainer {

  static final String TEST_EMAIL = "test@example.com";
  static final String TEST_PROVIDER = "google";
  static final String TEST_PROVIDER_ID = "12345";

  @Autowired
  private UserRepository userRepository;

  private UserService userService;

  private UserSocialLoginService userSocialLoginService;

  @BeforeEach
  void setUp() {
    userRepository.deleteAllInBatch();
    userService = new UserService(userRepository);
    userSocialLoginService = new UserSocialLoginService(userService);
  }

  @Test
  @DisplayName("Should create new user account with USER ROLE linked to the social login")
  void shouldCreateNewAccountLinkedToSocial() {
    OAuth2UserInfo oAuth2UserInfo = createOAuth2UserInfo();

    userSocialLoginService.linkOrRegisterSocialAccount(oAuth2UserInfo);

    var findNewUser = userService.findBySocialLoginsProviderAndSocialLoginsProviderId(
        oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

    assertThat(findNewUser).isPresent().get().satisfies(user -> {
      assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
      assertThat(user.getRole()).isEqualTo(Role.USER);
      assertTrue(userService.existsByEmail(user.getEmail()));
      assertThat(user.getSocialLogins()).hasSize(1).first()
          .extracting(UserSocialLogin::getProvider, UserSocialLogin::getProviderId)
          .contains(TEST_PROVIDER, TEST_PROVIDER_ID);
    });
  }

  @Test
  @DisplayName("Should link social login to existing account")
  void shouldLinkSocialLoginToExistingAccount() {
    //given a registered user
    UserPrincipal existingUser = new UserPrincipal();
    existingUser.setEmail(TEST_EMAIL);
    userService.saveUser(existingUser);

    //when success login with social login with same email
    OAuth2UserInfo oAuth2UserInfo = createOAuth2UserInfo();

    userSocialLoginService.linkOrRegisterSocialAccount(oAuth2UserInfo);

    //should link the social login to the user account
    var userWithLinkedSocial = userService.findBySocialLoginsProviderAndSocialLoginsProviderId(
        oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

    assertThat(userWithLinkedSocial).isPresent().get().satisfies(user -> {
      assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
      assertThat(user.getSocialLogins()).hasSize(1).first()
          .extracting(UserSocialLogin::getProvider, UserSocialLogin::getProviderId)
          .containsExactly(TEST_PROVIDER, TEST_PROVIDER_ID);
    });
  }

  @Test
  @DisplayName("Should only return user when social login already linked")
  void shouldJustReturnUserAccountWhenSocialLoginAlreadyLinked() {
    //given a registered user
    UserPrincipal existingUser = new UserPrincipal();
    existingUser.setEmail(TEST_EMAIL);
    //and already registered social for that user
    UserSocialLogin social = new UserSocialLogin();
    social.setProvider(TEST_PROVIDER);
    social.setProviderId(TEST_PROVIDER_ID);
    social.setUser(existingUser);
    userService.saveUser(existingUser);

    OAuth2UserInfo oAuth2UserInfo = createOAuth2UserInfo();

    userSocialLoginService.linkOrRegisterSocialAccount(oAuth2UserInfo);

    var userWithSocialLinked = userService.findBySocialLoginsProviderAndSocialLoginsProviderId(
        TEST_PROVIDER, TEST_PROVIDER_ID);

    assertThat(userWithSocialLinked).isPresent().get().extracting(UserPrincipal::getSocialLogins)
        .asInstanceOf(InstanceOfAssertFactories.list(UserSocialLogin.class)).hasSize(1).first()
        .extracting(UserSocialLogin::getProvider, UserSocialLogin::getProviderId)
        .containsExactly(TEST_PROVIDER, TEST_PROVIDER_ID);
  }


  OAuth2UserInfo createOAuth2UserInfo() {
    OAuth2UserInfo userInfo = new OAuth2UserInfo();
    userInfo.setEmail(TEST_EMAIL);
    userInfo.setProvider(TEST_PROVIDER);
    userInfo.setProviderId(TEST_PROVIDER_ID);
    return userInfo;
  }
}
