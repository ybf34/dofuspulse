package com.dofuspulse.api.items.controller;

import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.service.contract.ItemDetailsService;
import jakarta.validation.Valid;
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
@ApiResponseDocumentation
public class ItemDetailsController {

  private final ItemDetailsService itemDetailsService;

  @GetMapping("/items/{id}")
  public ResponseEntity<ItemDetailsDto> getItemDetailsById(@PathVariable Long id) {
    return itemDetailsService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/items")
  public ResponseEntity<Page<ItemDetailsDto>> getAllItemDetails(
      @Valid ItemDetailsSearchCriteria params,
      Pageable pageable) {
    Page<ItemDetailsDto> itemPage = itemDetailsService.findAll(params, pageable);
    return ResponseEntity.ok(itemPage);
  }

}
