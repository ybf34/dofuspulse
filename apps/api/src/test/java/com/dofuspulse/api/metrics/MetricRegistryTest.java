package com.dofuspulse.api.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.CraftCostCalculator;
import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.calculator.ItemPerformanceCalculator;
import com.dofuspulse.api.metrics.calculator.MetricCalculator;
import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {
    MetricRegistry.class,
    ProfitMetricsCalculator.class,
    ItemPerformanceCalculator.class,
    DailySalesCalculator.class,
    CraftCostCalculator.class,
})
@DisplayName("Metric Registry Test")
@ActiveProfiles("test")
class MetricRegistryTest {

  @Autowired
  private MetricRegistry metricRegistry;

  @Test
  @DisplayName("Should register metric calculators for all metric types")
  void shouldRegisterAllMetricsCalculators() {
    int metricsTypeCount = MetricType.values().length;

    Map<MetricType, MetricCalculator<?, ?>> calculators = metricRegistry.getCalculators();

    System.out.println(
        "MetricType count: " + metricsTypeCount + "\nCalculators count: " + calculators.size());

    assertEquals(metricsTypeCount, calculators.size());
  }
}
