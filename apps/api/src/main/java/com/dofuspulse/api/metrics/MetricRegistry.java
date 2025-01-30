package com.dofuspulse.api.metrics;

import com.dofuspulse.api.metrics.calculator.MetricCalculator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MetricRegistry {

  private final Map<MetricType, MetricCalculator<?, ?>> calculators;

  public MetricRegistry(List<MetricCalculator<?, ?>> calculators) {
    this.calculators = calculators.stream()
        .collect(Collectors.toMap(MetricCalculator::getType, Function.identity()));
  }

  public <I, R> R calculate(MetricType metricType, I parameters) {

    @SuppressWarnings("unchecked")
    MetricCalculator<I, R> calculator = (MetricCalculator<I, R>) calculators.get(metricType);

    if (calculator == null) {
      throw new IllegalArgumentException("No calculator for " + metricType);
    }

    if (!calculator.getInputType().isInstance(parameters)) {
      throw new IllegalArgumentException("Invalid input type");
    }
    return calculator.calculate(parameters);
  }
}
