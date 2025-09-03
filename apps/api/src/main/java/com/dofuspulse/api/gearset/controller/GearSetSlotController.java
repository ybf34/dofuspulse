package com.dofuspulse.api.gearset.controller;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.gearset.dto.EquipItemRequest;
import com.dofuspulse.api.gearset.dto.GearSetSlotDto;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponseDocumentation
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GearSetSlotController {

  private final GearSetSlotService gearSetSlotService;

  @PostMapping("/gearsets/{gearSetId}/slots")
  public ResponseEntity<GearSetSlotDto> equipItem(
      @PathVariable Long gearSetId,
      @RequestBody @Valid EquipItemRequest equipItemRequest,
      @AuthenticationPrincipal UserPrincipal user) {

    GearSetSlotDto equippedItem = gearSetSlotService.equipItem(equipItemRequest, gearSetId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(equippedItem);
  }

  @DeleteMapping("/gearsets/{gearSetId}/slots/{slotId}")
  public ResponseEntity<Void> unequipItem(
      @PathVariable Long gearSetId,
      @PathVariable Long slotId,
      @AuthenticationPrincipal UserPrincipal user) {
    gearSetSlotService.unequipItem(gearSetId, slotId, user);
    return ResponseEntity.noContent().build();
  }
}
