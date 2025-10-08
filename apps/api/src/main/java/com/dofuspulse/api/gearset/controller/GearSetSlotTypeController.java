package com.dofuspulse.api.gearset.controller;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GearSetSlotTypeController {

  private final GearSetSlotTypeService gearSetSlotTypeService;

  @GetMapping("/gearset-slot-types")
  public ResponseEntity<List<GearSetSlotTypeDto>> getAllGearSetSlotTypes() {
    return ResponseEntity.ok(gearSetSlotTypeService.findSlotTypes());
  }
}
