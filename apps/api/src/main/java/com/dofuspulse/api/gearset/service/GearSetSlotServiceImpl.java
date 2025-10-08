package com.dofuspulse.api.gearset.service;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.ItemTypeIncompatibilityException;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotService;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetRepository;
import com.dofuspulse.api.repository.GearSetSlotRepository;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GearSetSlotServiceImpl implements GearSetSlotService {

  private final ItemDetailsRepository itemDetailsRepository;
  private final GearSetRepository gearSetRepository;
  private final GearSetSlotRepository slotRepository;
  private final GearSetSlotTypeRepository slotTypeRepository;

  @Override
  @Transactional
  public GearSetSlotDto equipItem(EquipItemRequest request, Long gearSetId, UserPrincipal user) {
    GearSet gearSet = gearSetRepository.findById(gearSetId)
        .filter(g -> g.getUserPrincipal().getId().equals(user.getId()))
        .orElseThrow(() -> new NoSuchElementException("GearSet not found"));

    GearSetSlotType gearSetSlotType = slotTypeRepository.findByName(
            GearSetSlotTypeIdentifier.valueOf(request.slotIdentifier().toUpperCase()))
        .orElseThrow(
            () -> new NoSuchElementException(
                "GearSet slot type not found with identifier " + request.slotIdentifier()));

    ItemDetails itemDetails = itemDetailsRepository.findById(request.itemId())
        .orElseThrow(
            () -> new ItemNotFoundException("Item with id " + request.itemId() + " not found"));

    boolean allowed = gearSetSlotType.getItemTypes()
        .stream()
        .map(ItemType::getId)
        .anyMatch(id -> id.equals(itemDetails.getItemTypeId()));

    if (!allowed) {
      throw new ItemTypeIncompatibilityException(
          "Item with id " + request.itemId() + " is not allowed for slot type "
              + gearSetSlotType.getName());
    }

    GearSetSlot newSlot = slotRepository.findFirstByGearSetAndGearSetSlotType(gearSet,
            gearSetSlotType)
        .orElse(new GearSetSlot());

    newSlot.setGearSet(gearSet);
    newSlot.setGearSetSlotType(gearSetSlotType);
    newSlot.setItemDetails(itemDetails);

    return new GearSetSlotDto(slotRepository.save(newSlot));
  }

  @Override
  @Transactional
  public void unequipItem(Long gearSetId, Long slotId, UserPrincipal user) {

    GearSetSlot ownedSlot = slotRepository.findByIdAndGearSetIdAndGearSetUserPrincipalId(
            slotId,
            gearSetId,
            user.getId())
        .orElseThrow(
            () -> new NoSuchElementException(
                "Slot " + slotId + " not found in GearSet " + gearSetId + " for the current user"));

    slotRepository.delete(ownedSlot);
  }
}
