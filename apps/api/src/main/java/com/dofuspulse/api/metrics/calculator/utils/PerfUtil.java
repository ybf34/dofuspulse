package com.dofuspulse.api.metrics.calculator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
   * @param values The array of metric values.
   * @param dates  The list of dates corresponding to the values.
   * @return The trend (slope) of the metric over time.
   */
  public static double calculateTrend(double[] values, Set<LocalDate> dates) {
    if (values.length == 0 || dates.isEmpty() || values.length != dates.size()) {
      return 0.0;
    }

    double min = values[0];
    double max = values[0];

    for (double value : values) {
      if (value < min) {min = value;}
      if (value > max) {max = value;}
    }

    if (min == max) {
      return 0.0;
    }
    return round(calculateSlope(values, dates, min, max));
  }

  private static double calculateSlope(
      double[] values,
      Set<LocalDate> dates,
      double min,
      double max) {
    int n = values.length;
    if (n < 2) {
      return 0.0;
    }

    LocalDate startDate = dates.iterator().next();
    double range = max - min;
    double sumX = 0;
    double sumY = 0;
    double sumXY = 0;
    double sumX2 = 0;

    int index = 0;
    for (LocalDate date : dates) {
      double x = ChronoUnit.DAYS.between(startDate, date);
      double y = (values[index] - min) / range;
      sumX += x;
      sumY += y;
      sumXY += x * y;
      sumX2 += x * x;
      index++;
    }

    return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
  }

  /**
   * Calculates the percentage change from an average.
   */
  public static double calculatePctChange(double currentValue, double average) {
    if (average == 0) {
      return 0;
    }

    return round(((currentValue - average) / average) * 100);
  }
}