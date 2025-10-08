package com.dofuspulse.api.gearset.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.CustomUserDetailsService;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.controller.GearSetSlotController;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotService;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;


@WebMvcTest(GearSetSlotController.class)
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
class GearSetSlotControllerUnitTest {

  private final String testUserEmail = "test@mail.com";
  @Autowired
  MockMvcTester mvcTester;
  @MockitoBean
  GearSetSlotService gearSetSlotService;
  @MockitoBean(name = "customUserDetailsService")
  CustomUserDetailsService customUserDetailsService;
  private UserPrincipal user;

  @BeforeEach
  void setUp() {
    user = new UserPrincipal();
    user.setId(1L);
    user.setEmail(testUserEmail);
    user.setRole(Role.USER);

    when(customUserDetailsService.loadUserByUsername(testUserEmail)).thenReturn(user);
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldEquipItemWith201Status() throws Exception {
    EquipItemRequest request = new EquipItemRequest("HAT", 10L);
    GearSetSlotDto mockSlot =
        new GearSetSlotDto(100L, null, null);

    when(
        gearSetSlotService.equipItem(any(EquipItemRequest.class), eq(1L), any(UserPrincipal.class)))
        .thenReturn(mockSlot);

    mvcTester
        .post()
        .uri("/api/v1/gearsets/{gearSetId}/slots", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.CREATED);

    ArgumentCaptor<EquipItemRequest> captor = ArgumentCaptor.forClass(EquipItemRequest.class);
    verify(gearSetSlotService).equipItem(captor.capture(), eq(1L), any(UserPrincipal.class));

    assertThat(captor.getValue().itemId()).isEqualTo(10L);
    assertThat(captor.getValue().slotIdentifier()).isEqualTo(GearSetSlotTypeIdentifier.HAT.toString());
  }

  @Test

  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenUnequippingNonExistentSlot() {
    doThrow(new NoSuchElementException("Slot not found"))
        .when(gearSetSlotService).unequipItem(eq(1L), eq(99L), any(UserPrincipal.class));

    mvcTester.delete()
        .uri("/api/v1/gearsets/{gearSetId}/slots/{slotId}", 1L, 99L)
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);

    verify(gearSetSlotService).unequipItem(eq(1L), eq(99L), any(UserPrincipal.class));
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenEquipItemForbiddenOrNotFoundGearset() throws Exception {
    EquipItemRequest request = new EquipItemRequest("HAT", 10L);
    when(gearSetSlotService.equipItem(any(), eq(1L), any(UserPrincipal.class)))
        .thenThrow(new NoSuchElementException("GearSet not found"));

    mvcTester.post()
        .uri("/api/v1/gearsets/{gearSetId}/slots", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);

    verify(gearSetSlotService).equipItem(any(), eq(1L), any(UserPrincipal.class));
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldUnequipItemWith204Status() {
    doNothing().when(gearSetSlotService).unequipItem(1L, 2L, user);

    mvcTester
        .delete()
        .uri("/api/v1/gearsets/{gearSetId}/slots/{slotId}", 1L, 2L)
        .assertThat()
        .hasStatus(HttpStatus.NO_CONTENT);

    verify(gearSetSlotService).unequipItem(eq(1L), eq(2L), any(UserPrincipal.class));
  }

  @ParameterizedTest(name = "Should return 400 when invalid EquipItemRequest: {0}")
  @MethodSource("com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory#invalidEquipItemRequests")
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn400ForInvalidEquipItemRequests(
      String displayName,
      EquipItemRequest invalidRequest) throws Exception {

    mvcTester.post()
        .uri("/api/v1/gearsets/{gearSetId}/slots", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(invalidRequest))
        .assertThat()
        .hasStatus(HttpStatus.BAD_REQUEST);

    verify(gearSetSlotService, times(0)).equipItem(any(), anyLong(), any());
  }
}
