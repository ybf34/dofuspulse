package com.dofuspulse.api.metrics.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.NotCraftableItemException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemProfitMetricsServiceImpl implements ItemProfitMetricsService {

  private final ItemDetailsRepository idr;
  private final ItemMarketEntryRepository imr;
  private final MetricRegistry metricRegistry;

  @Override
  @Transactional(readOnly = true)
  public List<ProfitMetrics> getItemProfitMetricsHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    ItemDetails itemDetails = idr.findById(itemId)
        .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found"));

    List<Long> ingredientIds = itemDetails.getIngredientIds();

    if (ingredientIds == null || ingredientIds.isEmpty()) {
      throw new NotCraftableItemException("Item with id " + itemId + " is not craftable");
    }

    List<ItemPrice> itemPrices = imr.getPriceHistoryInDateRangeForItems(List.of(itemId), startDate,
        endDate);

    List<CraftCost> itemCraftCosts = imr.getItemCraftCost(List.of(itemId), startDate, endDate);

    return metricRegistry.calculate(MetricType.PROFIT_METRICS,
        new ProfitMetricsParams(itemPrices, itemCraftCosts));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProfitMetricsList> getItemsProfitMetricsHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate) {

    List<Long> craftableItemsIds = idr.findAll(
            ItemDetailsSpecificationBuilder.buildSpecification(params))
        .stream()
        .filter(item -> item.getIngredientIds() != null)
        .map(ItemDetails::getId)
        .toList();

    List<ProfitMetricsList> itemProfitMetricsList = new ArrayList<>();

    for (Long itemId : craftableItemsIds) {
      List<CraftCost> itemCraftCosts = imr.getItemCraftCost(List.of(itemId), startDate, endDate);
      List<ItemPrice> itemPrices = imr.getPriceHistoryInDateRangeForItems(List.of(itemId),
          startDate, endDate);

      if (itemPrices.isEmpty() || itemCraftCosts.isEmpty()) {
        continue;
      }

      List<ProfitMetrics> itemProfitMetrics = metricRegistry.calculate(
          MetricType.PROFIT_METRICS,
          new ProfitMetricsParams(itemPrices, itemCraftCosts));

      itemProfitMetricsList.add(new ProfitMetricsList(itemId, itemProfitMetrics));
    }
    return itemProfitMetricsList;
  }
}