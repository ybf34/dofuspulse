package com.dofuspulse.api.gearset.fixtures;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GearSetScenarioFactory {

  public static final String mockEmail = "test@test.com";

  public static void deleteAll(
      UserRepository userRepository,
      CharacterClassRepository characterClassRepository,
      GearSetRepository gearSetRepository,
      GearSetSlotRepository gearSetSlotRepository,
      GearSetSlotTypeRepository gearSetSlotTypeRepository,
      ItemTypeRepository itemTypeRepository,
      ItemDetailsRepository itemDetailsRepository) {
    userRepository.deleteAll();
    gearSetSlotRepository.deleteAll();
    itemDetailsRepository.deleteAll();
    itemTypeRepository.deleteAll();
    gearSetSlotTypeRepository.deleteAll();
    characterClassRepository.deleteAll();
    gearSetRepository.deleteAll();
  }

  public static GearSetScenario createBasicGearSetScenario(
      UserRepository userRepository,
      CharacterClassRepository characterClassRepository,
      GearSetRepository gearSetRepository,
      GearSetSlotRepository gearSetSlotRepository,
      GearSetSlotTypeRepository gearSetSlotTypeRepository,
      ItemTypeRepository itemTypeRepository,
      ItemDetailsRepository itemDetailsRepository) {

    UserPrincipal user = new UserPrincipal();
    user.setEmail(mockEmail);
    user.setRole(Role.USER);
    userRepository.save(user);

    CharacterClass characterClass = characterClassRepository.save(
        GearSetTestDataFactory.createMockCharacterClass("cra")
    );

    GearSet gearSet = gearSetRepository.save(
        GearSetTestDataFactory.createMockGearSet(user)
    );

    ItemType itemType = itemTypeRepository.save(new ItemType(1L, "Amulet"));

    GearSetSlotType slotType = gearSetSlotTypeRepository.save(
        GearSetTestDataFactory.createMockSlotType("Amulet Slot", List.of(itemType))
    );

    ItemDetails itemDetails = itemDetailsRepository.save(
        ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of())
    );

    GearSetSlot slot = gearSetSlotRepository.save(
        GearSetTestDataFactory.createMockGearSetSlot(gearSet, slotType, itemDetails)
    );

    return new GearSetScenario(user, characterClass, gearSet, itemType, slotType, itemDetails,
        List.of(slot));
  }

  public static Stream<Arguments> invalidCreateGearSetRequests() {
    return Stream.of(
        Arguments.of("Blank title", new CreateGearSetRequest("", "cra", "m", List.of())),
        Arguments.of("Blank class", new CreateGearSetRequest("Set1", "", "m", List.of())),
        Arguments.of("Invalid gender", new CreateGearSetRequest("Set1", "cra", "x", List.of())),
        Arguments.of("Too many tags", new CreateGearSetRequest("Set1", "cra", "m",
            List.of("t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11"))),
        Arguments.of("Invalid tag pattern", new CreateGearSetRequest("Set1", "cra", "m",
            List.of("UPPERCASE"))),
        Arguments.of("Tag too long", new CreateGearSetRequest("Set1", "cra", "m",
            List.of("a".repeat(21))))
    );
  }

  public static Stream<Arguments> invalidEquipItemRequests() {
    return Stream.of(
        Arguments.of("Null slotTypeId", new EquipItemRequest(null, 10L)),
        Arguments.of("Null itemId", new EquipItemRequest(1L, null)),
        Arguments.of("Both null", new EquipItemRequest(null, null))
    );
  }

  public record GearSetScenario(
      UserPrincipal user,
      CharacterClass characterClass,
      GearSet gearSet,
      ItemType itemType,
      GearSetSlotType slotType,
      ItemDetails itemDetails,
      List<GearSetSlot> slots
  ) {}
}