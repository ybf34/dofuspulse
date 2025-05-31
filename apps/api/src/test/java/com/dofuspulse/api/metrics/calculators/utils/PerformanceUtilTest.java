package com.dofuspulse.api.metrics.calculators.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dofuspulse.api.metrics.calculator.utils.PerfUtil;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class PerformanceUtilTest {

  @Test
  void shouldRoundCorrectly() {
    assertEquals(1.235, PerfUtil.round(1.23456));
    assertEquals(-1.234, PerfUtil.round(-1.23412));
    assertEquals(0.0, PerfUtil.round(0.0));
    assertEquals(1.235, PerfUtil.round(1.2345));
  }

  @Test
  void shouldCalculateCorrectPositiveTrend() {

    double[] values = {10.0, 20.0, 30.0};
    Set<LocalDate> dates = createConsecutiveDates();

    double trend = PerfUtil.calculateTrend(values, dates);
    assertTrue(trend > 0);

    assertEquals(0.5, trend, 0.001);
  }

  @Test
  void shouldCalculateCorrectNegativeTrend() {
    double[] values = {30.0, 20.0, 10.0};
    Set<LocalDate> dates = createConsecutiveDates();

    double trend = PerfUtil.calculateTrend(values, dates);
    assertTrue(trend < 0);
    assertEquals(-0.5, trend, 0.001);
  }

  @Test
  void shouldReturnZeroTrendForConstantValues() {
    double[] values = {25.0, 25.0, 25.0};
    Set<LocalDate> dates = createConsecutiveDates();
    assertEquals(0.0, PerfUtil.calculateTrend(values, dates));
  }

  @Test
  void shouldHandleEmptyArrays() {
    double[] values = {};
    Set<LocalDate> dates = new TreeSet<>();
    assertEquals(0.0, PerfUtil.calculateTrend(values, dates));
  }

  @Test
  void shouldHandleSingleDataPoint() {
    double[] values = {100.0};
    Set<LocalDate> dates = new TreeSet<>(List.of(LocalDate.of(2023, 1, 1)));
    assertEquals(0.0, PerfUtil.calculateTrend(values, dates));
  }

  @Test
  void shouldHandleNonConsecutiveDates() {
    double[] values = {10.0, 30.0};
    Set<LocalDate> dates = new TreeSet<>(Arrays.asList(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 11)
    ));

    double trend = PerfUtil.calculateTrend(values, dates);
    assertTrue(trend > 0, "Should handle non-consecutive dates correctly");
  }

  @Test
  void shouldComputePctChangeCorrectly() {
    assertEquals(10.0, PerfUtil.calculatePctChange(110, 100));
    assertEquals(-10.0, PerfUtil.calculatePctChange(90, 100));
    assertEquals(0.0, PerfUtil.calculatePctChange(100, 100));
    assertEquals(0.0, PerfUtil.calculatePctChange(50, 0));

    assertEquals(20.0, PerfUtil.calculatePctChange(120, 100));
    assertEquals(-50.0, PerfUtil.calculatePctChange(50, 100));

    double result = PerfUtil.calculatePctChange(116.6666, 100);
    assertEquals(16.667, result, 0.001);
  }

  @Test
  void shouldCalculateTrendWithRealWorldData() {
    // Simulate 5 days of profit margins declining
    double[] profitMargins = {1000.0, 950.0, 900.0, 850.0, 800.0};
    Set<LocalDate> dates = new TreeSet<>(Arrays.asList(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 2),
        LocalDate.of(2023, 1, 3),
        LocalDate.of(2023, 1, 4),
        LocalDate.of(2023, 1, 5)
    ));

    double trend = PerfUtil.calculateTrend(profitMargins, dates);
    assertTrue(trend < 0);

    assertEquals(-0.25, trend, 0.001);
  }

  private Set<LocalDate> createConsecutiveDates() {
    return new TreeSet<>(Arrays.asList(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 2),
        LocalDate.of(2023, 1, 3)
    ));
  }
}
