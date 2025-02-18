package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.BaseParams;

public interface MetricCalculator<I extends BaseParams, R> {

  MetricType getType();

  Class<I> getInputType();

  R calculate(I input);
}
