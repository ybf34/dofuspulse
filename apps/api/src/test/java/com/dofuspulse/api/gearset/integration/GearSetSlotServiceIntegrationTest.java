package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.ItemTypeIncompatibilityException;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory;
import com.dofuspulse.api.gearset.fixtures.GearSetScenarioFactory.GearSetScenario;
import com.dofuspulse.api.gearset.service.GearSetSlotServiceImpl;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotService;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GearSetSlotServiceIntegrationTest extends PostgresIntegrationTestContainer {

  GearSetScenario gearSetScenario;

  GearSetSlotService gearSetSlotService;

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

    gearSetSlotService = new GearSetSlotServiceImpl(
        itemDetailsRepository,
        gearSetRepository,
        gearSetSlotRepository,
        gearSetSlotTypeRepository);
  }

  @Test
  void shouldEquipItem() {

    EquipItemRequest equipItemRequest = new EquipItemRequest(
        gearSetScenario.slotType().getName().toString(),
        gearSetScenario.itemDetails().getId()
    );

    GearSet gearSet = gearSetScenario.gearSet();
    Long gearSetId = gearSet.getId();
    UserPrincipal owner = gearSetScenario.user();

    GearSetSlotDto slot = gearSetSlotService.equipItem(equipItemRequest, gearSetId, owner);

    assertThat(gearSetSlotRepository.findById(slot.id()))
        .isPresent()
        .get()
        .satisfies(persisted -> {
          assertThat(persisted.getGearSet().getId()).isEqualTo(gearSetId);
          assertThat(persisted.getGearSetSlotType().getId()).isEqualTo(
              gearSetScenario.slotType().getId());
          assertThat(persisted.getItemDetails().getId()).isEqualTo(
              gearSetScenario.itemDetails().getId());
        });
  }

  @Test
  void shouldThrowWhenGearSetNotFound() {
    EquipItemRequest request =
        new EquipItemRequest(
            gearSetScenario.slotType().getName().toString(), gearSetScenario.itemDetails().getId());

    assertThatThrownBy(() -> gearSetSlotService.equipItem(request, 999L, gearSetScenario.user()))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenUserDoesNotOwnGearSet() {
    UserPrincipal otherUser = new UserPrincipal();
    otherUser.setEmail("other@mail.com");
    otherUser.setRole(Role.USER);
    userRepository.save(otherUser);

    EquipItemRequest request =
        new EquipItemRequest(
            gearSetScenario.slotType().getName().toString(), gearSetScenario.itemDetails().getId());

    assertThatThrownBy(
        () -> gearSetSlotService.equipItem(request, gearSetScenario.gearSet().getId(), otherUser))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenSlotTypeNotFound() {
    EquipItemRequest request = new EquipItemRequest("BOOTS", gearSetScenario.itemDetails().getId());

    assertThatThrownBy(
        () -> gearSetSlotService.equipItem(request, gearSetScenario.gearSet().getId(),
            gearSetScenario.user()))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenItemNotFound() {
    EquipItemRequest request = new EquipItemRequest(gearSetScenario.slotType().getName().toString(), 999L);

    assertThatThrownBy(
        () -> gearSetSlotService.equipItem(request, gearSetScenario.gearSet().getId(),
            gearSetScenario.user()))
        .isInstanceOf(ItemNotFoundException.class);
  }

  @Test
  void shouldThrowWhenItemTypeIncompatibleWithSlotType() {
    ItemType ringType = itemTypeRepository.save(new ItemType(2L, "RING"));
    ItemDetails ringItem =
        itemDetailsRepository.save(
            new ItemDetails(
                2L, "Ring", 100L, 200L, ringType.getId(), List.of(), List.of(), List.of()));

    EquipItemRequest request =
        new EquipItemRequest(gearSetScenario.slotType().getName().toString(), ringItem.getId());

    assertThatThrownBy(
        () -> gearSetSlotService.equipItem(request, gearSetScenario.gearSet().getId(),
            gearSetScenario.user()))
        .isInstanceOf(ItemTypeIncompatibilityException.class);
  }

  @Test
  void shouldUnequipItem() {
    GearSetSlot existingSlot = gearSetScenario.slots().getFirst();
    Long gearSetId = gearSetScenario.gearSet().getId();
    Long slotId = existingSlot.getId();
    UserPrincipal owner = gearSetScenario.user();

    gearSetSlotService.unequipItem(gearSetId, slotId, owner);

    assertThat(gearSetSlotRepository.findById(slotId)).isEmpty();
  }

  @Test
  void shouldThrowWhenUnequippingSlotNotOwnedByUser() {
    UserPrincipal otherUser = new UserPrincipal();
    otherUser.setEmail("other@mail.com");
    otherUser.setRole(Role.USER);
    userRepository.save(otherUser);

    GearSetSlot existingSlot = gearSetScenario.slots().getFirst();

    assertThatThrownBy(() ->
        gearSetSlotService.unequipItem(
            gearSetScenario.gearSet().getId(),
            existingSlot.getId(),
            otherUser
        )
    ).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenUnequippingNonExistentSlot() {
    Long gearSetId = gearSetScenario.gearSet().getId();
    Long nonExistentSlotId = 999L;

    assertThatThrownBy(() ->
        gearSetSlotService.unequipItem(gearSetId, nonExistentSlotId, gearSetScenario.user())
    ).isInstanceOf(NoSuchElementException.class);
  }
}
