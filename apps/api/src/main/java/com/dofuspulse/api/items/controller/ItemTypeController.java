package com.dofuspulse.api.items.controller;

import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.service.contract.ItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ItemTypeController {

  private final ItemTypeService itemTypeService;

  @GetMapping("/item-types/{typeId}")
  public ResponseEntity<ItemTypeDto> getItemTypeById(@PathVariable Long typeId) {
    return itemTypeService.getItemTypeById(typeId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());

  }

  @GetMapping("/item-types")
  public ResponseEntity<Page<ItemTypeDto>> getAllItemTypes(Pageable pageable) {
    Page<ItemTypeDto> itemTypesPage = itemTypeService.getItemTypes(pageable);
    return ResponseEntity.ok(itemTypesPage);
  }
}
