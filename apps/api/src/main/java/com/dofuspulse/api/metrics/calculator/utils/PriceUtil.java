package com.dofuspulse.api.metrics.calculator.utils;

import java.util.List;
import java.util.stream.IntStream;

public class PriceUtil {

  private PriceUtil() {
    throw new AssertionError("Utility class cannot be instantiated.");
  }

  /**
   * Calculates the minimum unit price among the item quantity/price pairs.
   *
   * @param prices The list of prices.
   * @return The minimum unit price.
   */
  public static Integer getMinimumUnitPrice(List<Integer> prices) {
    if (prices == null || prices.isEmpty()) {
      return 0;
    }

    return IntStream
        .range(0, prices.size())
        .map(idx ->
            (int) (prices.get(idx) / Math.pow(10, idx))
        )
        .filter(p -> p > 0)
        .min()
        .orElse(0);
  }
}
