package com.dofuspulse.api.market.controller;

import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.market.service.contract.ItemPriceHistoryService;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ApiResponseDocumentation
public class ItemPriceHistoryController {

  private final ItemPriceHistoryService itemPriceHistoryService;

  @GetMapping("/items/{id}/price-history")
  public ResponseEntity<List<ItemPrice>> getItemPriceHistory(
      @PathVariable Long id,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(itemPriceHistoryService.getItemPriceHistory(id, startDate, endDate));
  }
}
