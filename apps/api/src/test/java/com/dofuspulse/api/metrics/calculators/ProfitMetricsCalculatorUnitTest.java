package com.dofuspulse.api.metrics.calculators;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.fixtures.scenarios.ProfitMetricsScenarioFactory;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Profit Metrics Calculator Unit Tests")
class ProfitMetricsCalculatorUnitTest {

  @Test
  @DisplayName("Should calculate and return correct profit metrics")
  void shouldCalculateAndReturnCorrectProfitMetrics() {

    ProfitMetricsCalculator calculator = new ProfitMetricsCalculator();
    var params = ProfitMetricsScenarioFactory.correctProfitMetricsCalculationScenario();

    List<ProfitMetrics> profitMetrics = calculator.calculate(params);

    assertAll(
        () -> assertEquals(2, profitMetrics.size()),
        () -> assertEquals(10000, profitMetrics.getFirst().sellPrice()),
        () -> assertEquals(1000, profitMetrics.getFirst().craftCost()),
        () -> assertEquals(9000, profitMetrics.getFirst().profitMargin())
    );
  }

  @Test
  @DisplayName("Should not return profit metrics if a craft cost is missing")
  void shouldNotReturnProfitsMetricsIfaCraftCostIsMissing() {

    ProfitMetricsCalculator calculator = new ProfitMetricsCalculator();
    ProfitMetricsParams params = ProfitMetricsScenarioFactory.missingCraftCostScenario();

    List<ProfitMetrics> profitMetrics = calculator.calculate(params);

    //1 profit metric object should be returned
    // as a craft cost is missing to perform the second day metrics calculation
    assertEquals(1, profitMetrics.size());
  }


  @Test
  @DisplayName("Should not return profit metrics for one date if one item price is missing")
  void shouldNotReturnProfitsMetricsIfItemPriceMissingForADate() {

    ProfitMetricsCalculator calculator = new ProfitMetricsCalculator();
    ProfitMetricsParams params = ProfitMetricsScenarioFactory.missingItemPriceScenario();

    List<ProfitMetrics> profitMetrics = calculator.calculate(params);

    assertEquals(1, profitMetrics.size());
  }

}
