package com.dofuspulse.api.metrics.service;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.metrics.service.contract.ItemPerformanceService;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemPerformanceServiceImpl implements ItemPerformanceService {

  private final ItemDetailsRepository idr;
  private final ItemDailySalesService salesService;
  private final ItemProfitMetricsService profitService;
  private final MetricRegistry metricRegistry;

  @Override
  @Transactional(readOnly = true)
  public ItemPerformance getItemPerformanceMetrics(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    List<DailySales> dailySales = salesService.getItemDailySalesHistory(itemId, startDate, endDate);
    List<ProfitMetrics> profitMetrics = profitService.getItemProfitMetricsHistory(itemId, startDate,
        endDate);

    return metricRegistry.<PerformanceMetricsParam, Optional<ItemPerformance>>calculate(
            MetricType.PERFORMANCE,
            new PerformanceMetricsParam(itemId, dailySales, profitMetrics))
        .orElseThrow(() -> new NoSuchElementException(
            String.format("No sales or profit metrics available for item ID %d in the range %s to %s.",
                itemId, startDate, endDate)));
  }


  @Override
  @Transactional(readOnly = true)
  public List<ItemPerformance> getItemsPerformanceMetrics(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate) {

    List<ItemDetails> items = idr.findAll(
        ItemDetailsSpecificationBuilder.buildSpecification(params));

    Map<Long, List<DailySales>> itemsDailySales = salesService.getItemsDailySalesHistory(params,
            startDate,
            endDate)
        .stream()
        .collect(Collectors.toMap(
            DailySalesList::itemId,
            DailySalesList::dailySales
        ));

    Map<Long, List<ProfitMetrics>> itemsProfitMetrics = profitService.getItemsProfitMetricsHistory(
            params,
            startDate,
            endDate)
        .stream()
        .collect(Collectors.toMap(
            ProfitMetricsList::itemId,
            ProfitMetricsList::profitMetrics
        ));

    return items.parallelStream()
        .filter(item ->
            itemsDailySales.containsKey(item.getId())
                && itemsProfitMetrics.containsKey(item.getId()))
        .map(item -> metricRegistry
            .<PerformanceMetricsParam, Optional<ItemPerformance>>calculate(
                MetricType.PERFORMANCE,
                new PerformanceMetricsParam(
                    item.getId(),
                    itemsDailySales.get(item.getId()),
                    itemsProfitMetrics.get(item.getId())
                )))
        .flatMap(Optional::stream)
        .toList();
  }

}
