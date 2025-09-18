package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.fixtures.GearSetTestDataFactory;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GearSetSlotTypeRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  ItemType mockItemType = new ItemType(1L, "AMULET");

  GearSetSlotType mockSlotType = GearSetTestDataFactory.createMockSlotType(
      GearSetSlotTypeIdentifier.AMULET,
      List.of(mockItemType));

  @Autowired
  GearSetSlotTypeRepository gearSetSlotTypeRepository;

  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    itemTypeRepository.save(mockItemType);
    gearSetSlotTypeRepository.save(mockSlotType);
  }

  @Test
  void shouldReturnGearSetSlotTypeByIdWithItemTypesLazyLoaded() {
    Optional<GearSetSlotType> slotType = gearSetSlotTypeRepository.findById(
        mockSlotType.getId());

    assertThat(slotType)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockSlotType);
  }

}
