package com.dofuspulse.api.metrics.fixtures.scenarios;

import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.fixtures.builders.ItemPriceBuilder;
import com.dofuspulse.api.projections.CraftCost;
import java.time.LocalDate;
import java.util.List;

public class ProfitMetricsScenarioFactory {

  public static ProfitMetricsParams correctProfitMetricsCalculationScenario() {
    return new ProfitMetricsParams(List.of(
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withPrices(10000, 0, 0, 0)
            .build(),
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withPrices(9000, 0, 0, 0)
            .build()
    ),
        List.of(
            new CraftCost(LocalDate.of(2023, 1, 1), 1000),
            new CraftCost(LocalDate.of(2023, 1, 2), 900)
        ));
  }

  public static ProfitMetricsParams missingCraftCostScenario() {
    return new ProfitMetricsParams(List.of(
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withPrices(10000, 0, 0, 0)
            .build(),
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withPrices(9000, 0, 0, 0)
            .build()
    ),
        List.of(
            new CraftCost(LocalDate.of(2023, 1, 1), 1000)
            //,new CraftCost(LocalDate.of(2023, 1, 2), 900)
        ));
  }
}
