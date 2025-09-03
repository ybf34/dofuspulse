package com.dofuspulse.api.gearset.controller;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.service.contract.GearSetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponseDocumentation
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GearSetController {

  private final GearSetService gearSetService;

  @GetMapping("/gearsets/{id}")
  public ResponseEntity<GearSetDto> getGearSetById(@PathVariable Long id) {
    return gearSetService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/user/gearsets")
  public ResponseEntity<List<GearSetDto>> getUserGearSets(@AuthenticationPrincipal UserPrincipal user) {
    List<GearSetDto> gearSets = gearSetService.findUserGearSets(user);
    return ResponseEntity.ok(gearSets);
  }

  @PostMapping("/gearsets")
  public ResponseEntity<GearSetDto> createGearSet(
      @RequestBody @Valid CreateGearSetRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    GearSetDto createdGearSet = gearSetService.createGearSet(request, userPrincipal);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdGearSet);
  }

  @DeleteMapping("/gearsets/{id}")
  public ResponseEntity<Void> deleteGearSet(
      @PathVariable Long id,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    gearSetService.deleteGearSet(id, userPrincipal);
    return ResponseEntity.noContent().build();
  }

}
