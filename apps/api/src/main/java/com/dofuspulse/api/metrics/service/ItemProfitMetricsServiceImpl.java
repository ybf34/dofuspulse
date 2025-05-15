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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemProfitMetricsServiceImpl implements ItemProfitMetricsService {

  private final ItemDetailsRepository idr;
  private final ItemMarketEntryRepository isr;
  private final MetricRegistry metricRegistry;

  @Override
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

    List<ItemPrice> itemPrices = isr.getPriceHistoryInDateRangeForItems(List.of(itemId), startDate,
        endDate);

    List<CraftCost> itemCraftCosts = isr.getItemCraftCost(List.of(itemId), startDate, endDate);

    return metricRegistry.calculate(MetricType.PROFIT_METRICS,
        new ProfitMetricsParams(itemPrices, itemCraftCosts));
  }

  @Override
  public List<ProfitMetricsList> getItemsProfitMetricsHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate) {

    List<ItemDetails> craftableItems = idr.findAll(
            ItemDetailsSpecificationBuilder.buildSpecification(params))
        .stream()
        .filter(item -> item.getIngredientIds() != null)
        .toList();

    List<Long> craftableItemsIds = craftableItems.stream()
        .map(ItemDetails::getId)
        .toList();

    Map<Long, List<CraftCost>> itemsCraftCosts = isr.getItemCraftCost(
            craftableItemsIds,
            startDate,
            endDate)
        .stream()
        .collect(Collectors.groupingBy(CraftCost::getItemId));

    Map<Long, List<ItemPrice>> itemsPrices = isr.getPriceHistoryInDateRangeForItems(
            craftableItemsIds, startDate, endDate)
        .stream()
        .collect(Collectors.groupingBy(ItemPrice::getItemId));

    return craftableItems.parallelStream()
        .filter(item -> itemsPrices.containsKey(item.getId()) && itemsCraftCosts.containsKey(
            item.getId()))
        .map(item -> {

          List<CraftCost> itemCraftCosts = itemsCraftCosts.get(item.getId());

          List<ProfitMetrics> itemProfitMetrics = metricRegistry.calculate(
              MetricType.PROFIT_METRICS,
              new ProfitMetricsParams(itemsPrices.get(item.getId()), itemCraftCosts));

          return Optional.of(new ProfitMetricsList(item.getId(), itemProfitMetrics));
        }).flatMap(Optional::stream).toList();
  }
}