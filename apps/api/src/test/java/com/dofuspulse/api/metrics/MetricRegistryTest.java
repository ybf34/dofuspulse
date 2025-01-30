package com.dofuspulse.api.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.MetricCalculator;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Metric Registry Test")
public class MetricRegistryTest {

  @Autowired
  private MetricRegistry metricRegistry;

  @Test
  @DisplayName("Should register all metrics calculators as there are metric types")
  public void shouldRegisterAllMetricsCalculators()
      throws NoSuchFieldException, IllegalAccessException {
    int metricsTypeCount = MetricType.values().length;

    Field calculatorsField = MetricRegistry.class.getDeclaredField("calculators");
    calculatorsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    Map<MetricType, MetricCalculator<?, ?>> calculatorsMap = (Map<MetricType, MetricCalculator<?, ?>>) calculatorsField.get(
        metricRegistry);

    System.out.println("MetricType count: " + metricsTypeCount);
    assertEquals(metricsTypeCount, calculatorsMap.size());

  }
}
