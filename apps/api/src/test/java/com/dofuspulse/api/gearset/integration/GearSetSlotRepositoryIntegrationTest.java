package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory.GearSetScenario;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GearSetSlotRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  GearSetScenario mockGearSetScenario;

  Long slotId;
  Long gearSetId;
  Long userId;

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
        gearSetSlotRepository,
        gearSetSlotTypeRepository, itemTypeRepository, itemDetailsRepository);

    mockGearSetScenario = GearSetScenarioFactory.createBasicGearSetScenario(userRepository,
        characterClassRepository,
        gearSetRepository,
        gearSetSlotRepository,
        gearSetSlotTypeRepository,
        itemTypeRepository,
        itemDetailsRepository);

    slotId = mockGearSetScenario.slots().getFirst().getId();
    gearSetId = mockGearSetScenario.gearSet().getId();
    userId = mockGearSetScenario.gearSet().getUserPrincipal().getId();
  }

  @Test
  void shouldReturnGearSetSlotBySlotIdAndGearSetIdAndUser() {

    Optional<GearSetSlot> gearSetSlot = gearSetSlotRepository.findByIdAndGearSetIdAndGearSetUserPrincipalId(
        slotId, gearSetId, userId);

    assertThat(gearSetSlot)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockGearSetScenario.slots().getFirst());
  }

  @Test
  void shouldReturnGearSetSlotFromGearSetAndSlotType() {

    Optional<GearSetSlot> slot = gearSetSlotRepository.findFirstByGearSetAndGearSetSlotType(
        mockGearSetScenario.gearSet(),
        mockGearSetScenario.slotType());

    assertThat(slot)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockGearSetScenario.slots().getFirst());
  }
}
