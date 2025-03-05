package com.dofuspulse.api.auth.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("User service integration test")
@DataJpaTest
public class UserServiceIntegrationTest extends PostgresIntegrationTestContainer {

  static String testEmail = "test@test.com";

  @Autowired
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    userService = new UserService(userRepository);
  }

  @Test
  void shouldSaveNewUser() {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setEmail(testEmail);
    newUser.setPassword("password".toCharArray());
    newUser.setRole(Role.USER);

    userService.saveUser(newUser);
    assertThat(userRepository.findByEmail(testEmail)).isPresent();
  }

  @Test
  void shouldGetUserByEmail() {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setEmail(testEmail);
    newUser.setPassword("password".toCharArray());
    newUser.setRole(Role.USER);
    userService.saveUser(newUser);

    assertThat(userService.findByEmail(testEmail))
        .isPresent()
        .get()
        .extracting(UserPrincipal::getEmail)
        .isEqualTo(testEmail);

  }

  @Test
  void shouldReturnEmptyOptionalIfEmailDoesNotExist() {
    assertThat(userService.findByEmail("nonexistent@test.com")).isEmpty();
  }

  @Test
  @DisplayName("Should return true when user exists by email")
  void shouldReturnTrueOnExistenceOfUserByEmail() {
    UserPrincipal newUser = new UserPrincipal();
    newUser.setEmail(testEmail);
    newUser.setPassword("password".toCharArray());
    newUser.setRole(Role.USER);

    userService.saveUser(newUser);

    assertThat(userService.existsByEmail(newUser.getEmail())).isTrue();
  }

  @Test
  @DisplayName("Should return false when user does not exist by email")
  void shouldReturnFalseForNonExistentUserByEmail() {
    assertThat(userService.existsByEmail(testEmail)).isFalse();
  }

}
