package com.dofuspulse.api.gearset.service.contract;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;

public interface GearSetSlotService {

  GearSetSlotDto equipItem(EquipItemRequest request, Long gearSetId, UserPrincipal user);

  void unequipItem(Long gearSetId, Long slotId, UserPrincipal user);
}
