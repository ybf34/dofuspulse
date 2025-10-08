package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory.GearSetScenario;
import com.dofuspulse.api.gearset.service.GearSetServiceImpl;
import com.dofuspulse.api.gearset.service.contract.GearSetService;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GearSetServiceIntegrationTest extends PostgresIntegrationTestContainer {

  GearSetScenario gearSetScenario;

  GearSetService gearSetService;

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

    gearSetService = new GearSetServiceImpl(gearSetRepository, characterClassRepository);
  }

  @Test
  void shouldReturnGearSetById() {
    GearSet expectedGearSet = gearSetScenario.gearSet();

    GearSetDto gearSet = gearSetService.findById(gearSetScenario.gearSet().getId(), gearSetScenario.user());

    assertThat(gearSet).isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(new GearSetDto(expectedGearSet));
  }

  @Test
  void shouldThrowExceptionWhenGearsetNotFound() {
    assertThatThrownBy(() -> gearSetService.findById(28L, gearSetScenario.user())).isInstanceOf(
        NoSuchElementException.class);
  }

  @Test
  void shouldReturnAllUserGearSets() {
    GearSet expectedGearSet = gearSetScenario.gearSet();

    List<GearSetDto> gearSet = gearSetService.findUserGearSets(gearSetScenario.user());

    assertThat(gearSet).hasSize(1).first().usingRecursiveComparison()
        .isEqualTo(new GearSetDto(expectedGearSet));
  }

  @Test
  void shouldCreateGearSetForUser() {
    String expectedTitle = "My gearset";
    CharacterClassName expectedClass = CharacterClassName.CRA;
    String expectedGender = "f";
    List<String> expectedTags = List.of("tag1", "tag2");

    CreateGearSetRequest createGearSetRequest = new CreateGearSetRequest(expectedTitle,
        expectedClass.toString(), expectedGender, expectedTags);

    GearSetDto newGearSet = gearSetService.createGearSet(createGearSetRequest,
        gearSetScenario.user());

    gearSetRepository.findById(newGearSet.id()).ifPresentOrElse(persisted -> {
      assertThat(persisted).satisfies(gs -> {
        assertThat(gs.getUserPrincipal().getId()).as("Ownership must match")
            .isEqualTo(gearSetScenario.user().getId());

        assertThat(gs.getTitle()).isEqualTo(expectedTitle);
        assertThat(gs.getCharacterClass().getName()).isEqualTo(expectedClass);
        assertThat(gs.getCharacterGender()).isEqualTo(expectedGender);
        assertThat(gs.getTags()).containsExactlyElementsOf(expectedTags);
      });

      assertThat(newGearSet).usingRecursiveComparison().isEqualTo(new GearSetDto(persisted));
    }, () -> Assertions.fail("Expected  new gearset to be persisted but not found"));
  }

  @Test
  void shouldUpdateGearset() {
    String newTitle = "gearset updated";
    CharacterClassName newClass = CharacterClassName.valueOf("CRA");
    String newGenre = "m";
    List<String> newTags = List.of("tag1", "tag2");

    UpdateGearSetRequest updateGearSetRequest = new UpdateGearSetRequest(
        newTitle,
        newClass.toString(),
        newGenre,
        newTags
    );

    GearSetDto updatedGearSet = gearSetService.updateGearSet(gearSetScenario.gearSet().getId(),
        updateGearSetRequest,
        gearSetScenario.user());

    gearSetRepository.findById(updatedGearSet.id())
        .ifPresent(gearset -> {
          assertThat(gearset).satisfies(gs -> {
            assertThat(gs.getUserPrincipal().getId()).as("Ownership must match")
                .isEqualTo(gearSetScenario.user().getId());

            assertThat(gs.getTitle()).isEqualTo(newTitle);
            assertThat(gs.getCharacterClass().getName()).isEqualTo(newClass);
            assertThat(gs.getCharacterGender()).isEqualTo(newGenre);
            assertThat(gs.getTags()).containsExactlyElementsOf(newTags);
          });

          assertThat(updatedGearSet)
              .usingRecursiveComparison()
              .isEqualTo(new GearSetDto(gearset));
        });
  }

  @Test
  void shouldDeleteGearSetWhenOwnedByUser() {
    GearSet gearSet = gearSetScenario.gearSet();
    Long gearSetId = gearSet.getId();
    UserPrincipal owner = gearSetScenario.user();

    gearSetService.deleteGearSet(gearSetId, owner);

    assertThat(gearSetRepository.findById(gearSetId)).isEmpty();
  }

  @Test
  void shouldThrowWhenDeletingGearSetNotOwnedByUser() {
    GearSet gearSet = gearSetScenario.gearSet();
    Long gearSetId = gearSet.getId();

    UserPrincipal anotherUser = new UserPrincipal();
    anotherUser.setEmail("other@mail.com");
    anotherUser.setRole(Role.USER);
    userRepository.save(anotherUser);

    assertThatThrownBy(() -> gearSetService.deleteGearSet(gearSetId, anotherUser)).isInstanceOf(
        NoSuchElementException.class);
  }
}
