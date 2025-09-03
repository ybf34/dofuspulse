package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory.GearSetScenario;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
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
public class GearSetSlotControllerIntegrationTest extends PostgresIntegrationTestContainer {

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
  void shouldEquipItemForUserGearSetWith200Status() throws Exception {
    EquipItemRequest request = new EquipItemRequest(
        gearSetScenario.slotType().getId(),
        gearSetScenario.itemDetails().getId()
    );

    mvcTester.post()
        .uri("/api/v1/gearsets/{id}/slots", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.CREATED);

    assertThat(gearSetSlotRepository.findAll())
        .hasSize(1)
        .first()
        .satisfies(slot -> {
          assertThat(slot.getGearSet().getId()).isEqualTo(gearSetScenario.gearSet().getId());
          assertThat(slot.getItemDetails().getId()).isEqualTo(
              gearSetScenario.itemDetails().getId());
        });
  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenProvidedIncorrectGearSetSlotType() throws Exception {

    gearSetSlotRepository.deleteAll();
    gearSetSlotTypeRepository.deleteAll();

    EquipItemRequest request = new EquipItemRequest(
        gearSetScenario.slotType().getId(),
        gearSetScenario.itemDetails().getId()
    );

    mvcTester.post()
        .uri("/api/v1/gearsets/{id}/slots", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);

    assertThat(gearSetSlotRepository.findAll())
        .hasSize(0);
  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenItemTypeIncompatibleWithSlotType() throws Exception {
    gearSetSlotRepository.deleteAll();

    ItemType ringItemType = itemTypeRepository.save(new ItemType(3L, "RING"));
    ItemDetails ringItemDetails = itemDetailsRepository.save(
        new ItemDetails(70L, "Ring", 100L, 200L, ringItemType.getId(), List.of(), List.of(),
            List.of())
    );

    //equiping a ring on amulet slot
    EquipItemRequest request = new EquipItemRequest(
        gearSetScenario.slotType().getId(),
        ringItemDetails.getId()
    );

    mvcTester.post()
        .uri("/api/v1/gearsets/{id}/slots", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);

    assertThat(gearSetSlotRepository.findAll())
        .isEmpty();
  }

  @Test
  @WithUserDetails(
      value = GearSetScenarioFactory.mockEmail,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldUnequipItemForUserGearSetWithNoContentStatus() {
    GearSetSlot existingSlot = gearSetScenario.slots().getFirst();

    mvcTester.delete()
        .uri("/api/v1/gearsets/{gearSetId}/slots/{slotId}",
            gearSetScenario.gearSet().getId(), existingSlot.getId())
        .assertThat()
        .hasStatus(HttpStatus.NO_CONTENT);

    assertThat(gearSetSlotRepository.findById(existingSlot.getId())).isEmpty();
  }

  @Test
  @WithUserDetails(
      value = user2Email,
      userDetailsServiceBeanName = "customUserDetailsService",
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void shouldReturn404WhenEquippingItemOnAnotherUsersGearSet() throws Exception {
    EquipItemRequest request = new EquipItemRequest(
        gearSetScenario.slotType().getId(),
        gearSetScenario.itemDetails().getId()
    );

    mvcTester.post()
        .uri("/api/v1/gearsets/{id}/slots", gearSetScenario.gearSet().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request))
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);
  }
}
