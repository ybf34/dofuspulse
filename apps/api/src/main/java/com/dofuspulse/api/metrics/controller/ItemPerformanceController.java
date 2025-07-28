package com.dofuspulse.api.metrics.controller;

import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.metrics.service.contract.ItemPerformanceService;
import com.dofuspulse.api.projections.ItemPerformance;
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
@ApiResponseDocumentation
public class ItemPerformanceController {

  private final ItemPerformanceService itemPerformanceService;

  @GetMapping("/items/{id}/performance")
  public ResponseEntity<ItemPerformance> getItemPerformanceMetrics(
      @PathVariable Long id,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return itemPerformanceService.getItemPerformanceMetrics(id, startDate, endDate)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/items/performance")
  public ResponseEntity<List<ItemPerformance>> getItemsPerformanceMetrics(
      @Valid ItemDetailsSearchCriteria params,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemPerformanceService.getItemsPerformanceMetrics(params, startDate,
            endDate));
  }

}
