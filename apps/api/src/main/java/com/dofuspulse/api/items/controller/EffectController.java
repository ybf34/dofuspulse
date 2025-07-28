package com.dofuspulse.api.items.controller;

import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.items.service.contract.EffectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ApiResponseDocumentation
public class EffectController {

  private final EffectService effectService;

  @GetMapping("/effects/{id}")
  public ResponseEntity<EffectDto> getEffectById(@PathVariable Long id) {
    return effectService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/effects")
  public ResponseEntity<List<EffectDto>> getAllEffects() {
    return ResponseEntity.ok(effectService.findAll());
  }

}
