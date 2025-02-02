package com.dofuspulse.api.controller;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.PriceHistory;
import com.dofuspulse.api.projections.ProfitMarginList;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import com.dofuspulse.api.repository.ItemTypeRepository;
import com.dofuspulse.api.service.TestService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final ItemTypeRepository itemTypeRepository;
  private final ItemDetailsRepository itemDetailsRepository;
  private final ItemSalesSnapshotRepository itemSalesSnapshotRepository;
  private final TestService testService;

  @GetMapping("/")
  public String test() {
    return "Dofus Pulse Api is running";
  }

  @GetMapping("/admin")
  public String test1(@AuthenticationPrincipal UserPrincipal principal) {

    return "Hello to the admin page " + principal.getEmail() + principal.getId()
        + principal.getRole().getAuthorities();

  }

  @GetMapping("/api/v1/items")
  public ResponseEntity<List<ItemDetails>> getAllItemDetails() {
    return ResponseEntity.ok(itemDetailsRepository.findAll());
  }

  @GetMapping("/api/v1/types/{typeId}/items")
  public ResponseEntity<List<ItemDetails>> getItemsByTypeId(@PathVariable @Validated Long typeId) {
    return ResponseEntity.ok(itemDetailsRepository.findAllByItemTypeId(typeId, ItemDetails.class));
  }

  @GetMapping("/api/v1/types")
  public ResponseEntity<List<ItemType>> getAllItemTypes() {

    return ResponseEntity.ok(itemTypeRepository.findAll());
  }

  @GetMapping("/api/v1/types/{id}")
  public ResponseEntity<ItemType> getItemTypeById(@PathVariable @Validated Long id) {
    return ResponseEntity.ok(itemTypeRepository.findById(id).orElseThrow());
  }

  @GetMapping("/api/v1/items/{id}/snapshots")
  public ResponseEntity<List<ItemSalesSnapshot>> getItemSnpByDate(
      @PathVariable @Validated Long id,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemSalesSnapshotRepository.findAllByItemIdAndSnapshotDateIsBetween(id, startDate,
            endDate, ItemSalesSnapshot.class));

  }

  @GetMapping("/api/v1/items/{id}/price-history")
  public ResponseEntity<List<PriceHistory>> getItemPriceHistory(
      @PathVariable @Validated Long id,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        itemSalesSnapshotRepository.getItemPriceHistoryInDateRange(id, startDate,
            endDate));

  }

  @GetMapping("/api/v1/items/{id}/craft-cost")
  public ResponseEntity<List<CraftCost>> getItemCraftCost(
      @PathVariable @Validated Long id,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        testService.getItemCraftCostInDateRange(id, startDate,
            endDate));

  }

  @GetMapping("/api/v1/items/{id}/profit-margin")
  public ResponseEntity<List<ProfitMetrics>> getItemProfitMargin(
      @PathVariable @Validated Long id,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        testService.getItemProfitMarginHistoryInDateRange(id, startDate,
            endDate));

  }

  @GetMapping("/api/v1/types/{typeId}/profit-margin")
  public ResponseEntity<List<ProfitMarginList>> getAllItemsProfitMarginHistoryByTypeInDateRange(
      @PathVariable @Validated Long typeId,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        testService.getAllItemsProfitMarginHistoryByTypeInDateRange(typeId, startDate,
            endDate));

  }

  @GetMapping("/api/v1/items/{id}/sales-history")
  public ResponseEntity<List<DailySales>> getItemSalesHistory(
      @PathVariable @Validated Long id,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @Validated @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return ResponseEntity.ok(
        testService.getItemDailySalesHistoryInDateRange(id, startDate,
            endDate));
  }

}
