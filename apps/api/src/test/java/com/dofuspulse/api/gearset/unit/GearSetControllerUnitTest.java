package com.dofuspulse.api.gearset.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.CustomUserDetailsService;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.controller.GearSetController;
import com.dofuspulse.api.gearset.dto.CharacterClassDto;
import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import com.dofuspulse.api.gearset.service.GearSetServiceImpl;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(GearSetController.class)
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class GearSetControllerUnitTest {

  final String testUserEmail = "test@mail.com";
  GearSetDto mockGearSet;
  @Autowired
  MockMvcTester mvcTester;
  @MockitoBean
  GearSetServiceImpl gearSetService;
  @MockitoBean(name = "customUserDetailsService")
  CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    mockGearSet = new GearSetDto(1L, "My gearset", new CharacterClassDto(CharacterClassName.CRA), "m", List.of(),
        List.of(), Instant.now(), Instant.now());

    UserPrincipal user = new UserPrincipal();
    user.setEmail(testUserEmail);
    user.setRole(Role.USER);

    when(customUserDetailsService.loadUserByUsername(testUserEmail))
        .thenReturn(user);
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldReturnGearSetById() {

    when(gearSetService.findById(eq(mockGearSet.id()), any(UserPrincipal.class))).thenReturn(mockGearSet);

    mvcTester.get()
        .uri("/api/v1/gearsets/{id}", mockGearSet.id())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(GearSetDto.class)
        .isEqualTo(mockGearSet);

    verify(gearSetService, times(1)).findById(eq(mockGearSet.id()), any(UserPrincipal.class));
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldReturn404WhenGearSetByIdNotFound() {

    when(gearSetService.findById(eq(mockGearSet.id()), any(UserPrincipal.class))).thenThrow(
        new NoSuchElementException(""));

    mvcTester.get()
        .uri("/api/v1/gearsets/{id}", mockGearSet.id())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);

    verify(gearSetService, times(1)).findById(eq(mockGearSet.id()), any(UserPrincipal.class));
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldReturnUserGearSetsWith200Status() {
    List<GearSetDto> gearSets = List.of(
        new GearSetDto(1L, "Set1", new CharacterClassDto(CharacterClassName.CRA), "m", List.of("tag1"), List.of(),
            Instant.now(), Instant.now()),
        new GearSetDto(2L, "Set2", new CharacterClassDto(CharacterClassName.CRA), "m", List.of("tag1"), List.of(),
            Instant.now(), Instant.now())
    );
    when(gearSetService.findUserGearSets(any(UserPrincipal.class))).thenReturn(gearSets);

    mvcTester.get()
        .uri("/api/v1/user/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.OK)
        .bodyJson()
        .hasPath("$[0].id")
        .hasPath("$[0].title");

    verify(gearSetService, times(1)).findUserGearSets(any(UserPrincipal.class));
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldCreateGearSetWith201Status() throws Exception {
    //given
    String expectedTitle = "My gearset";
    String expectedClass = "cra";
    String expectedGender = "f";
    List<String> expectedTags = List.of("tag1", "tag2");

    CreateGearSetRequest createGearSetRequest = new CreateGearSetRequest(
        expectedTitle,
        expectedClass,
        expectedGender,
        expectedTags
    );

    when(gearSetService.createGearSet(any(CreateGearSetRequest.class), any(UserPrincipal.class)))
        .thenReturn(mockGearSet);

    //when
    mvcTester.post().uri("/api/v1/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(createGearSetRequest))
        .assertThat()
        .hasStatus(201)
        .bodyJson()
        .hasPath("$.id")
        .hasPath("$.title")
        .hasPath("$.characterClass")
        .hasPath("$.characterGender")
        .hasPath("$.tags")
        .hasPath("$.slots");


    //then
    ArgumentCaptor<CreateGearSetRequest> captor = ArgumentCaptor.forClass(
        CreateGearSetRequest.class);
    verify(gearSetService, times(1)).createGearSet(captor.capture(), any(UserPrincipal.class));

    assertThat(captor.getValue())
        .satisfies(captured -> {
          assertThat(captured.title()).isEqualTo(expectedTitle);
          assertThat(captured.characterClass()).isEqualTo(expectedClass);
          assertThat(captured.characterGender()).isEqualTo(expectedGender);
          assertThat(captured.tags()).containsExactlyElementsOf(expectedTags);
        });
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldUpdateGearsetWith200Status() throws Exception {
    //given
    String newTitle = "gearset updated";
    String newClass = "iop";
    String newGenre = "m";
    List<String> newTags = List.of("tag1", "tag2");

    UpdateGearSetRequest updateGearSetRequest = new UpdateGearSetRequest(
        newTitle,
        newClass,
        newGenre,
        newTags
    );

    when(gearSetService.updateGearSet(eq(mockGearSet.id()), any(UpdateGearSetRequest.class),
        any(UserPrincipal.class)))
        .thenReturn(mockGearSet);

    //when
    mvcTester.put()
        .uri("/api/v1/gearsets/{id}", mockGearSet.id())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(updateGearSetRequest))
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.id")
        .hasPath("$.title")
        .hasPath("$.characterClass")
        .hasPath("$.characterGender")
        .hasPath("$.tags")
        .hasPath("$.slots");

    //then
    ArgumentCaptor<UpdateGearSetRequest> captor = ArgumentCaptor.forClass(
        UpdateGearSetRequest.class);

    verify(gearSetService, times(1)).updateGearSet(eq(mockGearSet.id()), captor.capture(),
        any(UserPrincipal.class));

    assertThat(captor.getValue())
        .satisfies(captured -> {
          assertThat(captured.title()).isEqualTo(newTitle);
          assertThat(captured.characterClass()).isEqualTo(newClass);
          assertThat(captured.characterGender()).isEqualTo(newGenre);
          assertThat(captured.tags()).containsExactlyElementsOf(newTags);
        });
  }

  @Test
  @WithUserDetails(
      value = testUserEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldDeleteGearSetWith204Status() {
    doNothing().when(gearSetService).deleteGearSet(eq(1L), any(UserPrincipal.class));

    mvcTester.delete()
        .uri("/api/v1/gearsets/{id}", 1L)
        .assertThat()
        .hasStatus(HttpStatus.NO_CONTENT);

    verify(gearSetService, times(1)).deleteGearSet(eq(1L), any(UserPrincipal.class));
  }

  @ParameterizedTest(name = "Should return 400 when invalid CreateGearSetRequest: {0}")
  @MethodSource("com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory#invalidCreateGearSetRequests")
  @WithMockUser
  void shouldReturn400ForInvalidCreateGearSetRequests(
      String displayName,
      CreateGearSetRequest invalidRequest) throws Exception {

    mvcTester.post()
        .uri("/api/v1/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(invalidRequest))
        .assertThat()
        .hasStatus(HttpStatus.BAD_REQUEST);

    verify(gearSetService, times(0)).createGearSet(any(), any());
  }

}
