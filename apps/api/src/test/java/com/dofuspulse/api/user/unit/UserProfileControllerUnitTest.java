package com.dofuspulse.api.user.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.CustomUserDetailsService;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserService;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import com.dofuspulse.api.user.controller.UserProfileController;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.dofuspulse.api.user.service.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;


@WebMvcTest(UserProfileController.class)
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
@Disabled
public class UserProfileControllerUnitTest {

  static final String testUserEmail = "test@test.com";
  UserPrincipal testUser;
  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  UserProfileServiceImpl userProfileService;

  @MockitoBean
  private UserService userService;

  @MockitoBean(name = "customUserDetailsService")
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    testUser = new UserPrincipal();
    testUser.setEmail(testUserEmail);
    testUser.setPassword("password".toCharArray());
    testUser.setRole(Role.USER);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider("google");
    userSocial.setProviderId("1234");
    userSocial.setUser(testUser);

    testUser.getSocialLogins().add(userSocial);
    when(customUserDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUser);
  }

  @Test
  @WithUserDetails(value = testUserEmail, userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturnUserProfile() {

    when(userProfileService.getUserProfile(any(UserPrincipal.class))).thenReturn(
        new UserProfileDto(testUser));

    mockMvcTester.get()
        .uri("/api/v1/user/me")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(userProfileService, times(1)).getUserProfile(any(UserPrincipal.class));
  }
}
