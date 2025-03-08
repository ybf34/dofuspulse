package com.dofuspulse.api.metrics.fixtures.scenarios;

import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.fixtures.builders.DailySalesBuilder;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.time.LocalDate;
import java.util.List;

public class PerformanceScenarioFactory {

  public static PerformanceMetricsParam validItemPerformanceScenario() {
    return new PerformanceMetricsParam(
        1L,
        List.of(
            DailySalesBuilder.builder()
                .withDate(LocalDate.of(2025, 1, 1))
                .withAdded(10)
                .withAvgSoldDuration(0)
                .withExpired(0)
                .withSold(0)
                .withRevenue(0)
                .withListingCount(10)
                .build(),
            DailySalesBuilder.builder()
                .withDate(LocalDate.of(2025, 1, 2))
                .withAdded(20)
                .withAvgSoldDuration(1)
                .withExpired(0)
                .withSold(10)
                .withRevenue(1000)
                .withListingCount(20)
                .build(),
            DailySalesBuilder.builder()
                .withDate(LocalDate.of(2025, 1, 3))
                .withAdded(5)
                .withAvgSoldDuration(1)
                .withExpired(0)
                .withSold(5)
                .withRevenue(500)
                .withListingCount(20)
                .build()
        ),
        List.of(
            new ProfitMetrics(LocalDate.of(2025, 1, 1), 200, 400, 200, 50.0),
            new ProfitMetrics(LocalDate.of(2025, 1, 2), 300, 600, 300, 50.0),
            new ProfitMetrics(LocalDate.of(2025, 1, 3), 100, 200, 100, 50.0)
        )
    );
  }

  public static PerformanceMetricsParam emptyDataScenario() {
    return new PerformanceMetricsParam(
        1L,
        List.of(),
        List.of()
    );
  }
}
