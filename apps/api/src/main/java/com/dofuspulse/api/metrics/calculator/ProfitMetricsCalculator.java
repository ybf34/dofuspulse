package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.util.List;
import java.util.Objects;
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
    return MetricType.PROFIT_MARGIN;
  }

  @Override
  public List<ProfitMetrics> calculate(ProfitMetricsParams data) {

    return data.itemPrices().stream().map(item -> data.craftCosts().stream()
        .filter(c -> c.snapshotDate().isEqual(item.getSnapshotDate())).findFirst().map(p -> {
          int sellingPrice = PriceUtil.getMinimumUnitPrice(item.getPrices());
          int craftCost = p.craftCost();
          int profit = sellingPrice - craftCost;
          double roi = (craftCost != 0) ? (double) profit / craftCost : 0;

          return new ProfitMetrics(p.snapshotDate(), craftCost, sellingPrice, profit, roi);
        }).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
  }
}
