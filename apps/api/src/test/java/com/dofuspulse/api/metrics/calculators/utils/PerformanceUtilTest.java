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
  }

  @Test
  void shouldReturnPositiveTrend() {
    List<Double> values = Arrays.asList(1.0, 2.0, 3.0);
    Set<LocalDate> dates = createDates();
    assertTrue(PerfUtil.calculateTrend(values, dates) > 0);
  }

  @Test
  void shouldReturnNegativeTrend() {
    List<Double> values = Arrays.asList(3.0, 2.0, 1.0);
    Set<LocalDate> dates = createDates();
    assertTrue(PerfUtil.calculateTrend(values, dates) < 0);
  }

  @Test
  void shouldReturnZeroTrend() {
    List<Double> values = Arrays.asList(2.0, 2.0, 2.0);
    Set<LocalDate> dates = createDates();
    assertEquals(0.0, PerfUtil.calculateTrend(values, dates));
  }

  @Test
  void shouldComputePctChangeCorrectly() {
    assertEquals(10.0, PerfUtil.calculatePctChange(110, 100));
    assertEquals(-10.0, PerfUtil.calculatePctChange(90, 100));
    assertEquals(0.0, PerfUtil.calculatePctChange(100, 100));
    assertEquals(0.0, PerfUtil.calculatePctChange(50, 0));
    assertEquals(16.667, PerfUtil.calculatePctChange(116.6666, 100));
  }

  private Set<LocalDate> createDates() {
    return new TreeSet<>(Arrays.asList(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 2),
        LocalDate.of(2023, 1, 3)
    ));
  }


}
