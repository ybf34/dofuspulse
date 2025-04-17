package com.dofuspulse.api.metrics.controller;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import jakarta.validation.Valid;
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
public class ItemDailySalesController {

  private final ItemDailySalesService itemDailySalesService;

  @GetMapping("/items/{id}/sales-history")
  public ResponseEntity<List<DailySales>> getItemSalesHistory(
      @PathVariable Long id,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    List<DailySales> dailySales = itemDailySalesService.getItemDailySalesHistory(id, startDate,
        endDate);

    return dailySales.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dailySales);
  }

  @GetMapping("/items/sales-history")
  public ResponseEntity<List<DailySalesList>> getItemsDailySalesHistory(
      @Valid ItemDetailsSearchCriteria params,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemDailySalesService.getItemsDailySalesHistory(params, startDate,
            endDate));
  }

}
