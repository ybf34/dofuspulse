package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
public class ProfitMetricsCalculator implements
    MetricCalculator<ProfitMetricsParams, List<ProfitMetrics>> {

  @Override
  public Class<ProfitMetricsParams> getInputType() {
    return ProfitMetricsParams.class;
  }

  @Override
  public MetricType getType() {
    return MetricType.PROFIT_METRICS;
  }

  @Override
  public List<ProfitMetrics> calculate(ProfitMetricsParams data) {

    Map<LocalDate, Integer> craftCostMap = data.craftCosts().stream()
        .collect(Collectors.toMap(CraftCost::snapshotDate, CraftCost::craftCost));

    return data.itemPrices().stream()
        .map(item -> {
          Optional<Integer> craftCostOpt = Optional.ofNullable(
              craftCostMap.get(item.getSnapshotDate()));
          return craftCostOpt.map(craftCost -> {
            int sellingPrice = PriceUtil.getMinimumUnitPrice(item.getPrices());
            int profit = sellingPrice - craftCost;
            double roi = (craftCost != 0) ? (double) profit / craftCost : 0;
            return new ProfitMetrics(item.getSnapshotDate(), craftCost, sellingPrice, profit, roi);
          });
        })
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }
}
