package com.dofuspulse.api.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.calculator.MetricCalculator;
import com.dofuspulse.api.metrics.calculator.PerformanceCalculator;
import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
    MetricRegistry.class,
    ProfitMetricsCalculator.class,
    PerformanceCalculator.class,
    DailySalesCalculator.class
})
@DisplayName("Metric Registry Test")
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
