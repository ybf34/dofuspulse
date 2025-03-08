package com.dofuspulse.api.metrics.calculator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PerfUtil {

  private PerfUtil() {
    throw new AssertionError("Utility class cannot be instantiated.");
  }

  /**
   * Rounds a double value to three decimal places.
   *
   * @param value The value to round.
   * @return The rounded value.
   */
  public static double round(double value) {
    BigDecimal bd = BigDecimal.valueOf(value);
    return bd.setScale(3, RoundingMode.HALF_UP).doubleValue();
  }

  /**
   * Calculates the trend (slope) of a metric over time using linear regression.
   *
   * @param values The list of metric values.
   * @param dates  The list of dates corresponding to the values.
   * @return The trend (slope) of the metric over time.
   */
  public static double calculateTrend(List<Double> values, Set<LocalDate> dates) {
    if (values.isEmpty() || dates.isEmpty()) {
      return 0.0;
    }

    double min = Collections.min(values);
    double max = Collections.max(values);

    if (min == max) {
      return 0.0;
    }
    // Normalize values to a 0–1 scale

    List<Double> normalizedValues = values.stream().map(value -> (value - min) / (max - min))
        .toList();

    return round(calculateSlope(normalizedValues, dates));
  }

  /**
   * Calculates the slope of the best-fit line using linear regression.
   *
   * @param x The independent variable (e.g., time).
   * @param y The dependent variable (e.g., metric values).
   * @return The slope of the best-fit line.
   */

  private static double calculateSlope(List<Double> values, Set<LocalDate> dates) {
    int n = values.size();
    if (n < 2) {
      return 0.0;
    }

    LocalDate startDate = dates.iterator().next();

    List<Long> daysSinceStart = dates.stream().map(date -> ChronoUnit.DAYS.between(startDate, date))
        .toList();

    double sumX = 0;
    double sumY = 0;
    double sumXY = 0;
    double sumX2 = 0;

    for (int i = 0; i < n; i++) {
      double x = daysSinceStart.get(i);
      double y = values.get(i);
      sumX += x;
      sumY += y;
      sumXY += x * y;
      sumX2 += x * x;
    }

    return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
  }

  /**
   * Calculates the percentage change from an average.
   *
   * @param currentValue The latest value.
   * @param average      The average value.
   * @return The percentage deviation.
   */
  public static double calculatePctChange(double currentValue, double average) {
    if (average == 0) {
      return 0;
    }

    return round(((currentValue - average) / average) * 100);
  }

}
