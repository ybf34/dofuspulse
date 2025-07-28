package com.dofuspulse.api.metrics.controller;

import com.dofuspulse.api.exception.ApiResponseDocumentation;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
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
public class ItemProfitMetricsController {

  private final ItemProfitMetricsService itemProfitMetricsService;

  @GetMapping("/items/{id}/profit-metrics")
  public ResponseEntity<List<ProfitMetrics>> getItemProfitMetricsHistory(
      @PathVariable Long id,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemProfitMetricsService.getItemProfitMetricsHistory(id, startDate,
            endDate));

  }

  @GetMapping("/items/profit-metrics")
  public ResponseEntity<List<ProfitMetricsList>> getItemsProfitMetricsHistory(
      @Valid ItemDetailsSearchCriteria params,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemProfitMetricsService.getItemsProfitMetricsHistory(params, startDate,
            endDate));

  }

}