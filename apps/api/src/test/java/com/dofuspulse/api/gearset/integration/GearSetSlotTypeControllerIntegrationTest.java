package com.dofuspulse.api.gearset.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.fixtures.GearSetTestDataFactory;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class GearSetSlotTypeControllerIntegrationTest extends PostgresIntegrationTestContainer {

  GearSetSlotType mockSlotType;

  @Autowired
  MockMvcTester mvcTester;

  @Autowired
  GearSetSlotTypeRepository slotTypeRepository;
  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    ItemType itemType = new ItemType(1L, "AMULET");
    mockSlotType = GearSetTestDataFactory.createMockSlotType(GearSetSlotTypeIdentifier.AMULET, List.of(itemType));
    itemTypeRepository.save(itemType);
    slotTypeRepository.save(mockSlotType);
  }

  @Test
  void shouldReturnAllSlotTypesWith200Status() {
    mvcTester.get()
        .uri("/api/v1/gearset-slot-types")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$[0].id")
        .hasPath("$[0].name")
        .hasPath("$[0].itemTypes");
  }
}
