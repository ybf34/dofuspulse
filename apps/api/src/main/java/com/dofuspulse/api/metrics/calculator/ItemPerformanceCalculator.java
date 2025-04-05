package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.calculator.utils.PerfUtil;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ItemPerformanceCalculator implements
    MetricCalculator<PerformanceMetricsParam, Optional<ItemPerformance>> {

  @Override
  public MetricType getType() {
    return MetricType.ITEM_PERFORMANCE;
  }

  @Override
  public Class<PerformanceMetricsParam> getInputType() {
    return PerformanceMetricsParam.class;
  }

  @Override
  public Optional<ItemPerformance> calculate(PerformanceMetricsParam data) {

    Map<LocalDate, DailySales> dailySalesMap = data.dailySales().stream()
        .collect(Collectors.toMap(DailySales::date, Function.identity()));

    Map<LocalDate, ProfitMetrics> profitMetricsMap = data.profitMetrics().stream()
        .collect(Collectors.toMap(ProfitMetrics::snapshotDate, Function.identity()));

    Set<LocalDate> dates = dailySalesMap.keySet().stream().filter(profitMetricsMap::containsKey)
        .collect(Collectors.toCollection(TreeSet::new));

    if (dates.isEmpty()) {
      return Optional.empty();
    }

    List<Double> profitMargins = new ArrayList<>();
    List<Double> craftCosts = new ArrayList<>();
    List<Double> rois = new ArrayList<>();
    List<Double> sellPrices = new ArrayList<>();
    List<Double> itemsSoldPerDay = new ArrayList<>();
    List<Double> listingCounts = new ArrayList<>();

    int totalItemsSold = 0;
    double totalSoldDuration = 0;
    double profitMarginSum = 0;
    double craftCostSum = 0;
    double listingPriceSum = 0;

    for (LocalDate date : dates) {
      DailySales ds = dailySalesMap.get(date);
      totalItemsSold += ds.sold();
      totalSoldDuration += ds.avgSoldDuration() * ds.sold();
      itemsSoldPerDay.add((double) ds.sold());
      listingCounts.add((double) ds.listingCount());

      ProfitMetrics pm = profitMetricsMap.get(date);
      profitMargins.add((double) pm.profitMargin());
      craftCosts.add((double) pm.craftCost());
      rois.add(pm.roi());
      sellPrices.add((double) pm.sellPrice());
      listingPriceSum += pm.sellPrice();
      profitMarginSum += pm.profitMargin();
      craftCostSum += pm.craftCost();
    }

    double salesVelocity = PerfUtil.round((double) totalItemsSold / dates.size());
    double avgDailyProfit = PerfUtil.round(profitMarginSum / dates.size());
    double avgSoldDuration =
        totalItemsSold > 0 ? PerfUtil.round(totalSoldDuration / totalItemsSold) : 0;
    double avgListingPrice = PerfUtil.round(listingPriceSum / dates.size());
    double avgCraftCost = PerfUtil.round(craftCostSum / dates.size());
    double costPctChange = PerfUtil.calculatePctChange(craftCosts.getLast(), avgCraftCost);
    double profitTrend = PerfUtil.calculateTrend(profitMargins, dates);
    double priceTrend = PerfUtil.calculateTrend(sellPrices, dates);
    double salesTrend = PerfUtil.calculateTrend(itemsSoldPerDay, dates);
    double listingsTrend = PerfUtil.calculateTrend(listingCounts, dates);
    double roiTrend = PerfUtil.calculateTrend(rois, dates);
    double craftCostTrend = PerfUtil.calculateTrend(craftCosts, dates);

    return Optional.of(new ItemPerformance(
        data.itemId(),
        totalItemsSold,
        salesVelocity,
        avgCraftCost,
        avgListingPrice,
        avgDailyProfit,
        profitTrend,
        craftCostTrend,
        priceTrend,
        salesTrend,
        listingsTrend,
        roiTrend,
        avgSoldDuration,
        costPctChange
    ));
  }
}