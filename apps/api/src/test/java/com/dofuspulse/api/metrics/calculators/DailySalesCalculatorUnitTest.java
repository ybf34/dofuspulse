package com.dofuspulse.api.metrics.calculators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.fixtures.scenarios.DailySalesScenarioFactory;
import com.dofuspulse.api.projections.DailySales;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the DailySalesCalculator
 */
@DisplayName("Daily Sales Calculator Unit Tests")
class DailySalesCalculatorUnitTest {

  /**
   * Test to ensure that no sales data returns an empty list
   */

  @Test
  @DisplayName("Should return an empty list if no sales data")
  void shouldReturnEmptyListIfNoSalesData() {

    DailySalesCalculator calculator = new DailySalesCalculator();
    List<DailySales> dailySales = calculator.calculate(new DailySalesParam(List.of()));

    assertThat(dailySales).isEmpty();
  }

  /**
   * Test that an item lives for 28 days in the market and then marked as expired to not conflict
   * with the sold items.
   *
   * <p> A unique item in the market is identified by its unique effects and price. If an item in a
   * future snapshot (date) disappears from the market, it is either counted as expired if the item
   * disappeared after 28 days (since entry date)  or marked as sold if it disappears before the
   * expiration date.</p>
   */

  @Test
  @DisplayName("Should expire an item after 28 days in the market")
  void itemShouldExpireAfter14daysInTheMarketIfNotBought() {
    DailySalesCalculator calculator = new DailySalesCalculator();

    DailySalesParam params = DailySalesScenarioFactory.expiredListingsScenario();

    List<DailySales> dailySales = calculator.calculate(params);

    assertAll(
        () -> assertEquals(1, dailySales.getFirst().added()),
        () -> assertEquals(1, dailySales.get(1).expired()),
        () -> assertEquals(0, dailySales.get(1).sold()),
        () -> assertEquals(0, dailySales.get(1).revenue())
    );
  }

  /**
   * Test that an item is marked as sold if it disappears from the market before 28 days.
   */

  @Test
  @DisplayName("Should sell an item if it disappears before 28 days")
  void itemShouldBeSoldIfItDisappearsBefore14Days() {
    DailySalesCalculator calculator = new DailySalesCalculator();

    DailySalesParam params = DailySalesScenarioFactory.soldListingsScenario();

    List<DailySales> dailySales = calculator.calculate(params);

    assertAll(
        () -> assertEquals(1, dailySales.getFirst().added()),
        () -> assertEquals(0, dailySales.getFirst().expired()),
        () -> assertEquals(1, dailySales.get(1).sold()),
        () -> assertEquals(1000, dailySales.get(1).revenue())
    );

  }

}
