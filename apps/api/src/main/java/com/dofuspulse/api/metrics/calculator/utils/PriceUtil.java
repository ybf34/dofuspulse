package com.dofuspulse.api.metrics.calculator.utils;

import java.util.List;
import java.util.stream.IntStream;

public class PriceUtil {

  public static Integer getMinimumUnitPrice(List<Integer> prices) {

    return IntStream
        .range(0, prices.size())
        .map(idx ->
            (int) (prices.get(idx) / Math.pow(10, idx))
        )
        .filter(p -> p > 0)
        .min()
        .orElseThrow();

  }
}
