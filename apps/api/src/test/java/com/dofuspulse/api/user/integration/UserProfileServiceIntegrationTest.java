package com.dofuspulse.api.user.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import com.dofuspulse.api.auth.oauth2.UserSocialLoginDto;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.dofuspulse.api.user.service.UserProfileServiceImpl;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@DataJpaTest
public class UserProfileServiceIntegrationTest extends PostgresIntegrationTestContainer {

  static final String testUserEmail = "test@test.com";
  static final String testSocialLoginProviderName = "google";
  static final String testSocialLoginUserProviderId = "1234";
  UserPrincipal testUser;
  @Autowired
  UserRepository userRepository;

  UserProfileServiceImpl userProfileService;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    testUser = new UserPrincipal();
    testUser.setEmail(testUserEmail);
    testUser.setPassword("password".toCharArray());
    testUser.setRole(Role.USER);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider(testSocialLoginProviderName);
    userSocial.setProviderId(testSocialLoginUserProviderId);
    userSocial.setUser(testUser);

    testUser.getSocialLogins().add(userSocial);
    userRepository.save(testUser);
    userProfileService = new UserProfileServiceImpl(userRepository);
  }

  @Test
  void shouldReturnUserProfile() {

    UserProfileDto userProfile = userProfileService.getUserProfile(testUser);

    assertThat(userProfile).extracting(UserProfileDto::id,
            UserProfileDto::email,
            UserProfileDto::role,
            UserProfileDto::socials,
            UserProfileDto::attributes,
            UserProfileDto::createdAt,
            UserProfileDto::updatedAt)
        .containsExactly(
            testUser.getId(),
            testUser.getEmail(),
            testUser.getRole(),
            testUser.getSocialLogins()
                .stream()
                .map(UserSocialLoginDto::new)
                .collect(Collectors.toList()),
            testUser.getAttributes(),
            testUser.getCreatedAt(),
            testUser.getUpdatedAt()
        );
  }


  @Test
  void shouldThrowUsernameNotFoundException() {
    UserPrincipal user = new UserPrincipal();
    user.setId(32L);

    assertThatThrownBy(() -> userProfileService.getUserProfile(user)).isInstanceOf(
        UsernameNotFoundException.class);
  }
}
