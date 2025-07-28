package com.dofuspulse.api.user.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.oauth2.UserSocialLogin;
import com.dofuspulse.api.auth.oauth2.UserSocialLoginDto;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.dofuspulse.api.user.service.UserProfileServiceImpl;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceUnitTest {

  UserPrincipal testUser;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserProfileServiceImpl userProfileService;

  @BeforeEach
  void setUp() {
    testUser = new UserPrincipal();
    testUser.setId(1L);
    testUser.setEmail("test@test.com");
    testUser.setPassword("password".toCharArray());
    testUser.setRole(Role.USER);

    UserSocialLogin userSocial = new UserSocialLogin();
    userSocial.setProvider("google");
    userSocial.setProviderId("1234");
    userSocial.setUser(testUser);

    testUser.getSocialLogins().add(userSocial);
  }

  @Test
  void shouldReturnUserProfile() {
    when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

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

    verify(userRepository, times(1)).findById(testUser.getId());
  }

  @Test
  void shouldThrowUsernameNotFoundException() {
    testUser.setId(32L);

    when(userRepository.findById(32L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userProfileService.getUserProfile(testUser)).isInstanceOf(
        UsernameNotFoundException.class);

    verify(userRepository, times(1)).findById(32L);

  }
}
