package com.dofuspulse.api.auth.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * User repository integration tests.
 */

@DataJpaTest
class UserRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  static String testEmail = "test@test.com";
  static String testSocialLoginProviderName = "google";
  static String testSocialLoginUserProviderId = "1234";

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    UserPrincipal testUser = new UserPrincipal();
    testUser.setEmail(testEmail);
    testUser.setPassword("password".toCharArray());
    testUser.setRole(Role.USER);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider(testSocialLoginProviderName);
    userSocial.setProviderId(testSocialLoginUserProviderId);
    userSocial.setUser(testUser);

    testUser.getSocialLogins().add(userSocial);
    userRepository.save(testUser);
  }

  /**
   * Test that the repository returns a user when the email exists.
   */
  @Test
  @DisplayName("Should return user by email")
  void shouldReturnUserByEmail() {
    Optional<UserPrincipal> user = userRepository.findByEmail(testEmail);

    assertThat(user)
        .isPresent()
        .get()
        .extracting(UserPrincipal::getEmail)
        .isEqualTo(testEmail);
  }

  /**
   * Test that the repository does not return a user when the email does not exist.
   */

  @Test
  @DisplayName("Should not find user by email")
  void shouldReturnEmptyOptionalWhenUserNotFoundByEmail() {
    Optional<UserPrincipal> user = userRepository.findByEmail("nonexistent@test.com");
    assertThat(user).isEmpty();
  }

  /**
   * Test that the repository does not return a user when the email does not exist.
   */

  @Test
  @DisplayName("Should return true on existence of user by email")
  void shouldReturnTrueOnExistenceOfUserByEmail() {
    assertThat(userRepository.existsByEmail(testEmail)).isTrue();
  }

  /**
   * Verifies that `existsByEmail` returns `false` when the email doesn't exist.
   */
  @Test
  @DisplayName("Should return false when user not found bye email")
  void shouldReturnFalseWhenUserNotFoundByEmail() {
    assertThat(userRepository.existsByEmail("nonexistentmail@test.com")).isFalse();
  }

  /**
   * Verifies that `findBySocialLoginsProviderAndSocialLoginsProviderId` returns the correct user
   * when searching by social login details.
   */
  @Test
  @DisplayName("Should return User based social login details (provider name/unique provider id)")
  void shouldFindUserBySocialLoginProviderAndProviderUniqueUserId() {
    assertThat(userRepository.findBySocialLoginsProviderAndSocialLoginsProviderId(
        testSocialLoginProviderName,
        testSocialLoginUserProviderId))
        .isPresent()
        .get()
        .extracting(UserPrincipal::getEmail)
        .isEqualTo(testEmail);
  }

  /**
   * Verifies that `findBySocialLoginsProviderAndSocialLoginsProviderId` returns an empty `Optional`
   * when the user is not linked to the specified social login.
   */
  @Test
  @DisplayName("Should return empty optional if user not linked to a social login")
  void shouldNotFindUserBySocialLoginProviderAndProviderUniqueUserId() {
    assertThat(
        userRepository.findBySocialLoginsProviderAndSocialLoginsProviderId("discord", "123456"))
        .isEmpty();
  }

}
