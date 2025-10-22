package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory.GearSetScenario;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GearSetControllerIntegrationTest extends PostgresIntegrationTestContainer {

  static final String user2Email = "user2@mail.com";
  GearSetScenario gearSetScenario;

  @Autowired
  MockMvcTester mvcTester;
  @Autowired
  UserRepository userRepository;
  @Autowired
  CharacterClassRepository characterClassRepository;
  @Autowired
  GearSetRepository gearSetRepository;
  @Autowired
  GearSetSlotRepository gearSetSlotRepository;
  @Autowired
  GearSetSlotTypeRepository gearSetSlotTypeRepository;
  @Autowired
  ItemDetailsRepository itemDetailsRepository;
  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    GearSetScenarioFactory.deleteAll(userRepository, characterClassRepository, gearSetRepository,
        gearSetSlotRepository, gearSetSlotTypeRepository, itemTypeRepository,
        itemDetailsRepository);

    gearSetScenario = GearSetScenarioFactory.createBasicGearSetScenario(userRepository,
        characterClassRepository, gearSetRepository, gearSetSlotRepository,
        gearSetSlotTypeRepository, itemTypeRepository, itemDetailsRepository);

    UserPrincipal user2 = new UserPrincipal();
    user2.setEmail(user2Email);
    user2.setRole(Role.USER);
    userRepository.save(user2);

  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturnGearSetByIdWith200Status() {

    mvcTester.get()
        .uri("/api/v1/gearsets/{id}", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(GearSetDto.class)
        .isEqualTo(new GearSetDto(gearSetScenario.gearSet()));
  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenGearSetNotFoundById() {
    mvcTester.get()
        .uri("/api/v1/gearsets/{id}", 9999L) // nonexistent
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);
  }


  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturnUserGearSetsWith200Status() {

    mvcTester.get().uri("/api/v1/user/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$[0].id")
        .hasPath("$[0].title")
        .hasPath("$[0].characterClass.name")
        .hasPath("$[0].characterGender")
        .hasPath("$[0].tags[0]")
        .hasPath("$[0].slots");

  }

  @Test
  @WithUserDetails(value = user2Email, userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturnEmptyListWhenUserHasNoGearSets() {
    mvcTester.get()
        .uri("/api/v1/user/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .isEqualTo("[]");
  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldCreateGearSetForUserWith201Status()
      throws JsonProcessingException {

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
    mvcTester.post().uri("/api/v1/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(createGearSetRequest))
        .assertThat()
        .hasStatus(201);

    List<GearSet> gearSets = gearSetRepository.findByUserPrincipalIdOrderByUpdatedAtDesc(
        gearSetScenario.user().getId());

    assertThat(gearSets)
        .hasSize(2)
        .anySatisfy(gs -> {
          assertThat(gs.getTitle()).isEqualTo(expectedTitle);
          assertThat(gs.getCharacterGender()).isEqualTo(expectedGender);
          assertThat(gs.getTags()).containsExactlyElementsOf(expectedTags);
        });

  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION
  )
  void shouldUpdateGearsetWith200Status() throws Exception {
    //given
    String newTitle = "gearset updated";
    String newClass = "CRA";
    String newGenre = "m";
    List<String> newTags = List.of("tag1", "tag2");

    UpdateGearSetRequest updateGearSetRequest = new UpdateGearSetRequest(
        newTitle,
        newClass,
        newGenre,
        newTags
    );

    //when
    mvcTester.put()
        .uri("/api/v1/gearsets/{id}", gearSetScenario.gearSet().getId())
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
        .hasPath("$.slots")
        .hasPath("$.createdAt")
        .hasPath("$.updatedAt");

  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn400WhenCreatingGearSetWithInvalidCharacterClass()
      throws JsonProcessingException {

    CreateGearSetRequest invalidRequest = new CreateGearSetRequest(
        "Invalid gearset",
        "nonexistentClass",
        "f",
        List.of("tag1")
    );

    mvcTester.post()
        .uri("/api/v1/gearsets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(invalidRequest))
        .assertThat()
        .hasStatus(HttpStatus.BAD_REQUEST);

    List<GearSet> gearSets = gearSetRepository.findByUserPrincipalIdOrderByUpdatedAtDesc(
        gearSetScenario.user().getId());
    assertThat(gearSets)
        .hasSize(1)
        .first()
        .extracting(GearSet::getTitle)
        .isEqualTo(gearSetScenario.gearSet().getTitle());

  }

  @Test
  @WithUserDetails(value = GearSetScenarioFactory.mockEmail, userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldDeleteUserGearSetWithNoContentStatus() {

    mvcTester.delete()
        .uri("/api/v1/gearsets/{id}", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.NO_CONTENT);

  }

  @Test
  @WithUserDetails(value = user2Email, userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenDeletingGearSetNotOwnedByCurrentUser() {

    mvcTester.delete()
        .uri("/api/v1/gearsets/{id}", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);
  }

  @Test
  @WithUserDetails(value = GearSetScenarioFactory.mockEmail, userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenDeletingNonexistentGearSet() {
    mvcTester.delete()
        .uri("/api/v1/gearsets/{id}", 9999L)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);
  }
}

