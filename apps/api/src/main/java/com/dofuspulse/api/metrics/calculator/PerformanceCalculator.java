package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.calculator.utils.PerfUtil;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PerformanceCalculator implements
    MetricCalculator<PerformanceMetricsParam, Optional<ItemPerformance>> {

  @Override
  public MetricType getType() {
    return MetricType.PERFORMANCE;
  }

  @Override
  public Class<PerformanceMetricsParam> getInputType() {
    return PerformanceMetricsParam.class;
  }

  @Override
  public Optional<ItemPerformance> calculate(PerformanceMetricsParam data) {

    if (data.dailySales().isEmpty() || data.profitMetrics().isEmpty()) {
      return Optional.empty();
    }

    Map<LocalDate, DailySales> salesMap = data.dailySales().stream()
        .collect(Collectors.toMap(DailySales::date, Function.identity()));

    Map<LocalDate, ProfitMetrics> profitMap = data.profitMetrics().stream()
        .collect(Collectors.toMap(ProfitMetrics::snapshotDate, Function.identity()));

    Set<LocalDate> commonDates = salesMap.keySet().stream()
        .filter(profitMap::containsKey)
        .collect(Collectors.toCollection(TreeSet::new));

    if (commonDates.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(calculatePerformance(data.itemId(), commonDates, salesMap, profitMap));
  }

  private ItemPerformance calculatePerformance(
      long itemId, Set<LocalDate> commonDates,
      Map<LocalDate, DailySales> salesMap,
      Map<LocalDate, ProfitMetrics> profitMap) {

    int totalItemsSold = 0;
    double totalWeightedDuration = 0;
    double profitMarginSum = 0;
    double craftCostSum = 0;
    double listingPriceSum = 0;

    int dayCount = commonDates.size();
    double[] profitMargins = new double[dayCount];
    double[] craftCosts = new double[dayCount];
    double[] rois = new double[dayCount];
    double[] sellPrices = new double[dayCount];
    double[] itemsSoldPerDay = new double[dayCount];
    double[] listingCounts = new double[dayCount];

    int index = 0;
    for (LocalDate date : commonDates) {
      DailySales ds = salesMap.get(date);
      ProfitMetrics pm = profitMap.get(date);

      totalItemsSold += ds.sold();
      totalWeightedDuration += ds.avgSoldDuration() * ds.sold();
      profitMarginSum += pm.profitMargin();
      craftCostSum += pm.craftCost();
      listingPriceSum += pm.sellPrice();

      profitMargins[index] = pm.profitMargin();
      craftCosts[index] = pm.craftCost();
      rois[index] = pm.roi();
      sellPrices[index] = pm.sellPrice();
      itemsSoldPerDay[index] = ds.sold();
      listingCounts[index] = ds.listingCount();

      index++;
    }

    double salesVelocity = PerfUtil.round((double) totalItemsSold / dayCount);
    double avgDailyProfit = PerfUtil.round(profitMarginSum / dayCount);
    double avgSoldDuration = totalItemsSold > 0 ? PerfUtil.round(totalWeightedDuration / totalItemsSold) : 0;
    double avgListingPrice = PerfUtil.round(listingPriceSum / dayCount);
    double avgCraftCost = PerfUtil.round(craftCostSum / dayCount);
    double costPctChange = dayCount > 0 ? PerfUtil.calculatePctChange(craftCosts[dayCount - 1], avgCraftCost) : 0;

    return new ItemPerformance(
        itemId,
        totalItemsSold,
        salesVelocity,
        avgCraftCost,
        avgListingPrice,
        avgDailyProfit,
        PerfUtil.calculateTrend(profitMargins, commonDates),
        PerfUtil.calculateTrend(craftCosts, commonDates),
        PerfUtil.calculateTrend(sellPrices, commonDates),
        PerfUtil.calculateTrend(itemsSoldPerDay, commonDates),
        PerfUtil.calculateTrend(listingCounts, commonDates),
        PerfUtil.calculateTrend(rois, commonDates),
        avgSoldDuration,
        costPctChange
    );
  }
}