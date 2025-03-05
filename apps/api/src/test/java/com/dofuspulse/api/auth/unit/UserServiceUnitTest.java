package com.dofuspulse.api.auth.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("User Service Unit Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

  static String testEmail = "test@test.com";

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Test
  @DisplayName("Should save user")
  void shouldSaveUser() {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setId(1L);
    newUser.setEmail(testEmail);
    newUser.setPassword("password".toCharArray());
    newUser.setRole(Role.USER);

    when(userRepository.save(any(UserPrincipal.class))).thenReturn(newUser);

    assertThat(userService.saveUser(newUser)).isNotNull()
        .extracting(UserPrincipal::getEmail, UserPrincipal::getRole)
        .containsExactly(newUser.getEmail(), newUser.getRole());

    verify(userRepository, times(1)).save(newUser);
  }

  @Test
  @DisplayName("Should return true when user exists by email")
  void shouldReturnTrueOnExistenceOfUserByEmail() {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setId(1L);
    newUser.setEmail(testEmail);
    newUser.setPassword("password".toCharArray());
    newUser.setRole(Role.USER);

    when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

    assertThat(userService.existsByEmail(newUser.getEmail())).isTrue();
    verify(userRepository, times(1)).existsByEmail(newUser.getEmail());
  }

  @Test
  @DisplayName("Should return false when user does not exist by email")
  void shouldReturnFalseForNonExistentUserByEmail() {
    when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

    assertThat(userService.existsByEmail(testEmail)).isFalse();
    verify(userRepository, times(1)).existsByEmail(testEmail);
  }

  @Test
  @DisplayName("Should return the user by email")
  void shouldReturnUserByEmail() {
    UserPrincipal user = new UserPrincipal();
    user.setId(1L);
    user.setEmail(testEmail);
    user.setPassword("password".toCharArray());
    user.setRole(Role.USER);

    when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

    assertThat(userService.findByEmail(user.getEmail())).isPresent();
    verify(userRepository, times(1)).findByEmail(user.getEmail());
  }

  @Test
  @DisplayName("Should return empty optional for non-existent user by email")
  void shouldReturnEmptyOptionalForNonExistentUserByEmail() {
    when(userRepository.findByEmail(any(String.class))).thenReturn(
        Optional.empty());

    assertThat(userService.findByEmail(testEmail)).isEmpty();
    verify(userRepository, times(1)).findByEmail(testEmail);
  }

}
