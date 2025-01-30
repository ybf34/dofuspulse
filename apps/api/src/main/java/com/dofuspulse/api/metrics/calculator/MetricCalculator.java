package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;

public interface MetricCalculator<I, R> {

  MetricType getType();

  Class<I> getInputType();

  R calculate(I input);
}
