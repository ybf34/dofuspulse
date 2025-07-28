package com.dofuspulse.api.user.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserProfileControllerIntegrationTest extends PostgresIntegrationTestContainer {

  static final String testUserEmail = "test@test.com";
  static final String testSocialLoginProviderName = "google";
  static final String testSocialLoginUserProviderId = "1234";

  @Autowired
  MockMvcTester mockMvcTester;
  Long savedUserId;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    UserPrincipal testUser = new UserPrincipal();
    testUser.setEmail(testUserEmail);
    testUser.setPassword("password".toCharArray());
    testUser.setRole(Role.USER);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider(testSocialLoginProviderName);
    userSocial.setProviderId(testSocialLoginUserProviderId);
    userSocial.setUser(testUser);

    testUser.getSocialLogins().add(userSocial);
    userRepository.save(testUser);
    UserPrincipal savedUser = userRepository.save(testUser);
    savedUserId = savedUser.getId();

  }

  @Test
  @WithUserDetails(value = testUserEmail, userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturnUserProfileWith200Success() {

    mockMvcTester.get()
        .uri("/api/v1/user/me")
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.id")
        .hasPath("$.email")
        .hasPath("$.role")
        .hasPath("$.socials")
        .hasPath("$.attributes")
        .hasPath("$.createdAt")
        .hasPath("$.updatedAt")
        .hasPath("$.socials[0].provider")
        .hasPath("$.socials[0].providerId");
  }
}
