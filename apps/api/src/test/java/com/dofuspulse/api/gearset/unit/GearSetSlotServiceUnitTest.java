package com.dofuspulse.api.gearset.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.ItemTypeIncompatibilityException;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.service.GearSetSlotServiceImpl;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GearSetSlotServiceUnitTest {

  @Mock
  ItemDetailsRepository itemDetailsRepository;
  @Mock
  GearSetRepository gearSetRepository;
  @Mock
  GearSetSlotRepository slotRepository;
  @Mock
  GearSetSlotTypeRepository slotTypeRepository;

  @InjectMocks
  GearSetSlotServiceImpl gearSetSlotService;

  UserPrincipal user;
  GearSet gearSet;
  GearSetSlotType slotType;
  ItemDetails item;

  @BeforeEach
  void setUp() {
    user = new UserPrincipal();
    user.setId(1L);

    gearSet = new GearSet();
    gearSet.setId(1L);
    gearSet.setUserPrincipal(user);

    ItemType itemType = new ItemType(1L, "Amulet");
    slotType = new GearSetSlotType();
    slotType.setName(GearSetSlotTypeIdentifier.AMULET);
    slotType.setId(1L);
    slotType.setItemTypes(List.of(itemType));

    item = new ItemDetails(10L, "item 1", 100L, 150L, 1L, List.of(), List.of(), List.of());

  }

  @Test
  void shouldEquipItem() {
    EquipItemRequest request = new EquipItemRequest(slotType.getName().toString(), item.getId());

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));
    when(slotTypeRepository.findByName(GearSetSlotTypeIdentifier.AMULET)).thenReturn(Optional.of(slotType));
    when(itemDetailsRepository.findById(10L)).thenReturn(Optional.of(item));
    when(slotRepository.findFirstByGearSetAndGearSetSlotType(gearSet, slotType))
        .thenReturn(Optional.empty());

    GearSetSlot saved = new GearSetSlot();
    saved.setId(99L);
    saved.setGearSet(gearSet);
    saved.setGearSetSlotType(slotType);
    saved.setItemDetails(item);
    when(slotRepository.save(any(GearSetSlot.class))).thenReturn(saved);

    GearSetSlotDto result = gearSetSlotService.equipItem(request, 1L, user);

    assertThat(result.id()).isEqualTo(99L);
  }

  @Test
  void shouldThrowWhenGearSetNotFound() {
    when(gearSetRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetSlotService.equipItem(new EquipItemRequest("AMULET", 10L), 1L, user))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenUserNotOwner() {
    UserPrincipal otherUser = new UserPrincipal();
    otherUser.setId(2L);
    gearSet.setUserPrincipal(otherUser);

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));

    assertThatThrownBy(() -> gearSetSlotService.equipItem(new EquipItemRequest("AMULET", 10L), 1L, user))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenSlotTypeNotFound() {
    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));
    when(slotTypeRepository.findByName(GearSetSlotTypeIdentifier.AMULET)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetSlotService.equipItem(new EquipItemRequest("AMULET", 10L), 1L, user))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldThrowWhenItemNotFound() {
    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));
    when(slotTypeRepository.findByName(GearSetSlotTypeIdentifier.AMULET))
        .thenReturn(Optional.of(slotType));
    when(itemDetailsRepository.findById(10L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetSlotService.equipItem(new EquipItemRequest("AMULET", 10L), 1L, user))
        .isInstanceOf(ItemNotFoundException.class);
  }

  @Test
  void shouldThrowWhenItemTypeIncompatible() {
    ItemDetails wrongItem = new ItemDetails();
    wrongItem.setId(20L);
    wrongItem.setItemTypeId(999L);

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));
    when(slotTypeRepository.findByName(GearSetSlotTypeIdentifier.AMULET)).thenReturn(Optional.of(slotType));
    when(itemDetailsRepository.findById(20L)).thenReturn(Optional.of(wrongItem));

    assertThatThrownBy(() -> gearSetSlotService.equipItem(new EquipItemRequest("AMULET", 20L), 1L, user))
        .isInstanceOf(ItemTypeIncompatibilityException.class);
  }

  @Test
  void shouldUnequipItem() {
    GearSetSlot slot = new GearSetSlot();
    slot.setId(5L);
    slot.setGearSet(gearSet);

    when(slotRepository.findByIdAndGearSetIdAndGearSetUserPrincipalId(5L, 1L, 1L))
        .thenReturn(Optional.of(slot));

    gearSetSlotService.unequipItem(1L, 5L, user);

    verify(slotRepository).delete(slot);
  }

  @Test
  void shouldThrowWhenSlotNotFoundOnUnequip() {
    when(slotRepository.findByIdAndGearSetIdAndGearSetUserPrincipalId(5L, 1L, 1L))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetSlotService.unequipItem(1L, 5L, user))
        .isInstanceOf(NoSuchElementException.class);
  }
}