package com.dofuspulse.api.metrics.calculators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.ItemPerformanceCalculator;
import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.fixtures.scenarios.PerformanceScenarioFactory;
import com.dofuspulse.api.projections.ItemPerformance;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Item Performance Calculator Unit Tests")
public class PerformanceCalculatorUnitTest {

  @Test
  @DisplayName("Should return item performance with zeros values if empty input data")
  void shouldReturnZerosValuesIfEmptyInputData() {
    //given
    PerformanceMetricsParam param = PerformanceScenarioFactory.emptyDataScenario();

    ItemPerformanceCalculator calculator = new ItemPerformanceCalculator();
    //when
    Optional<ItemPerformance> itemPerformanceOpt = calculator.calculate(param);

    assertThat(itemPerformanceOpt).isEmpty();

  }

  @Test
  @DisplayName("Should compute and return item performances metric correctly")
  void shouldComputeAndReturnPerformancesMetricCorrectly() {
    //given
    PerformanceMetricsParam param = PerformanceScenarioFactory.validItemPerformanceScenario();

    ItemPerformanceCalculator calculator = new ItemPerformanceCalculator();
    //when
    Optional<ItemPerformance> itemPerformanceOpt = calculator.calculate(param);

    assertThat(itemPerformanceOpt).isPresent();

    ItemPerformance itemPerformance = itemPerformanceOpt.get();
    // then
    assertAll(
        () -> assertEquals(200.0, itemPerformance.avgCraftCost()),
        () -> assertEquals(200.0, itemPerformance.avgDailyProfitMargin()),
        () -> assertEquals(400.0, itemPerformance.avgListingPrice()),
        () -> assertEquals(15, itemPerformance.salesVolume()),
        () -> assertEquals(5, itemPerformance.salesVelocity()),
        () -> assertEquals(1, itemPerformance.avgSoldDuration()),
        () -> assertEquals(-50, itemPerformance.craftCostPctChangeFromAvg()),
        () -> assertEquals(0.25, itemPerformance.salesTrend()),
        () -> assertEquals(0.25, itemPerformance.salesTrend()), // Corrected
        () -> assertEquals(-0.25, itemPerformance.priceTrend()),
        () -> assertEquals(-0.25, itemPerformance.profitMarginTrend()),
        () -> assertEquals(-0.25, itemPerformance.craftCostTrend()),
        () -> assertEquals(0, itemPerformance.roiTrend()),
        () -> assertEquals(0.5, itemPerformance.listingsTrend())
    );
  }
}
