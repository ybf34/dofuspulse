package com.dofuspulse.api.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.NotCraftableItemException;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.projections.PriceHistory;
import com.dofuspulse.api.projections.ProfitMarginList;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

  private final ItemDetailsRepository idr;
  private final ItemSalesSnapshotRepository isr;
  private final MetricRegistry metricCalculatorService;

  public List<CraftCost> getItemCraftCostInDateRange(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    ItemDetails itemDetails = idr.findById(itemId)
        .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found"));

    List<Long> ingredientIds = itemDetails.getIngredientIds();
    List<Integer> quantities = itemDetails.getQuantities();

    if (ingredientIds == null || ingredientIds.isEmpty()) {
      throw new NotCraftableItemException("Item with id " + itemId + " is not craftable");
    }

    Map<Long, Integer> ingredientQuantityMap = IntStream.range(0, ingredientIds.size()).boxed()
        .collect(Collectors.toMap(ingredientIds::get, quantities::get));

    List<ItemPrice> ingredientsPrices = isr.getItemsSnapshotsByIdsInDateRange(ingredientIds,
        startDate, endDate);

    return metricCalculatorService.calculate(MetricType.CRAFT_COST,
        new CraftCostParams(ingredientsPrices, ingredientQuantityMap));

  }

  public List<ProfitMetrics> getItemProfitMarginHistoryInDateRange(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    List<CraftCost> itemCraftCosts = getItemCraftCostInDateRange(itemId, startDate, endDate);
    List<PriceHistory> itemPrices = isr.getItemPriceHistoryInDateRange(itemId, startDate, endDate);

    return metricCalculatorService.calculate(MetricType.PROFIT_MARGIN,
        new ProfitMetricsParams(itemPrices, itemCraftCosts));
  }

  public List<ProfitMarginList> getAllItemsProfitMarginHistoryByTypeInDateRange(
      Long typeId,
      LocalDate startDate,
      LocalDate endDate) {

    List<Long> items = idr.findAllByItemTypeId(typeId, ItemDetails.class).stream()
        .filter(item -> item.getLevel() > 100).map(ItemDetails::getId).toList();

    return items.stream().map(id -> {
      try {
        List<ProfitMetrics> margins = getItemProfitMarginHistoryInDateRange(id, startDate, endDate);
        double avgRoi = margins.stream().mapToDouble(ProfitMetrics::roi).average().orElse(0);
        return new ProfitMarginList(id, margins, avgRoi);
      } catch (Exception e) {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public List<DailySales> getItemDailySalesHistoryInDateRange(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {
    List<ItemSalesSnapshot> salesData = isr.findAllByItemIdAndSnapshotDateIsBetween(itemId,
        startDate, endDate, ItemSalesSnapshot.class);

    return metricCalculatorService.calculate(MetricType.DAILY_SALES,
        new DailySalesParam(salesData));
  }

}
