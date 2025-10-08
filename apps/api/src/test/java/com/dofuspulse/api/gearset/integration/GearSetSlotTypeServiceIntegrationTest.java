package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.fixtures.GearSetTestDataFactory;
import com.dofuspulse.api.gearset.service.GearSetSlotTypeServiceImpl;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotTypeService;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GearSetSlotTypeServiceIntegrationTest extends PostgresIntegrationTestContainer {

  GearSetSlotType mockSlotType;

  GearSetSlotTypeService gearSetSlotTypeService;

  @Autowired
  GearSetSlotTypeRepository slotTypeRepository;

  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    gearSetSlotTypeService = new GearSetSlotTypeServiceImpl(slotTypeRepository);
    ItemType itemType = new ItemType(1L, "AMULET");
    mockSlotType = GearSetTestDataFactory.createMockSlotType(GearSetSlotTypeIdentifier.AMULET, List.of(itemType));
    itemTypeRepository.save(itemType);
    slotTypeRepository.save(mockSlotType);
  }

  @Test
  void shouldReturnAllSlotTypes() {

    List<GearSetSlotTypeDto> slots = gearSetSlotTypeService.findSlotTypes();

    assertThat(slots)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(mockSlotType);
  }

}
