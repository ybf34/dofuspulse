package com.dofuspulse.api.metrics.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.NotCraftableItemException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemProfitMetricsServiceImpl implements ItemProfitMetricsService {

  private final ItemDetailsRepository idr;
  private final ItemSalesSnapshotRepository isr;
  private final MetricRegistry metricRegistry;

  @Override
  public List<ProfitMetrics> getItemProfitMetricsHistory(
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

    List<ItemPrice> itemPrices = isr.getPriceHistoryInDateRangeForItems(List.of(itemId), startDate,
        endDate);

    List<CraftCost> itemCraftCosts = metricRegistry.calculate(MetricType.CRAFT_COST,
        new CraftCostParams(ingredientsPrices, ingredientQuantityMap));

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

    Map<Long, List<Long>> itemsIngredientsMap = craftableItems.stream()
        .collect(
            Collectors.groupingBy(ItemDetails::getId,
                Collectors.flatMapping(item -> item.getIngredientIds().stream(),
                    Collectors.toList())));

    List<Long> allUniqueIngredientsIds = itemsIngredientsMap.values()
        .stream()
        .distinct()
        .flatMap(List::stream)
        .toList();

    Map<Long, List<ItemPrice>> itemsIngredientsPrices = isr.getItemsSnapshotsByIdsInDateRange(
            allUniqueIngredientsIds,
            startDate,
            endDate)
        .stream()
        .collect(Collectors.groupingBy(ItemPrice::getItemId));

    List<Long> craftableItemsIds = itemsIngredientsMap.keySet()
        .stream()
        .toList();

    Map<Long, List<ItemPrice>> itemsPrices = isr.getPriceHistoryInDateRangeForItems(
            craftableItemsIds, startDate, endDate)
        .stream()
        .collect(Collectors.groupingBy(ItemPrice::getItemId));

    return craftableItems.parallelStream()
        .filter(item -> itemsPrices.containsKey(item.getId()))
        .map(item -> {

          List<Long> ingredientIds = item.getIngredientIds();
          List<Integer> quantities = item.getQuantities();

          Map<Long, Integer> ingredientQuantityMap = IntStream.range(0, ingredientIds.size())
              .boxed()
              .collect(Collectors.toMap(ingredientIds::get, quantities::get));

          List<ItemPrice> itemIngredientsPrices = ingredientIds.stream()
              .flatMap(
                  id -> itemsIngredientsPrices.getOrDefault(id, Collections.emptyList()).stream())
              .toList();

          List<CraftCost> itemCraftCosts = metricRegistry.calculate(MetricType.CRAFT_COST,
              new CraftCostParams(itemIngredientsPrices, ingredientQuantityMap));

          List<ProfitMetrics> itemProfitMetrics = metricRegistry.calculate(
              MetricType.PROFIT_METRICS,
              new ProfitMetricsParams(itemsPrices.get(item.getId()), itemCraftCosts));

          return Optional.of(new ProfitMetricsList(item.getId(), itemProfitMetrics));
        }).flatMap(Optional::stream).toList();
  }
}