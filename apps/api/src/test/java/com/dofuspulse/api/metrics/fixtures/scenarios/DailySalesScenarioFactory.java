package com.dofuspulse.api.metrics.fixtures.scenarios;

import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.fixtures.builders.ItemMarketEntryBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DailySalesScenarioFactory {

  /**
   * Mock data for an item added on Day 1 that expires on Day 15 (14-day window). The second
   * snapshot on Day 17 should trigger expiration count as the item no longer exists
   */
  public static DailySalesParam expiredListingsScenario() {
    LocalDate baseDate = LocalDate.of(2023, 1, 1);
    return new DailySalesParam(List.of(
        ItemMarketEntryBuilder.builder()
            .withItemId(2L)
            .withDate(baseDate)
            .withPrices(1000)
            .withEffects(Map.of("100", "1", "200", "2"))
            .build(),
        ItemMarketEntryBuilder.builder()
            .withDate(baseDate.plusDays(30))
            .withItemId(2L)
            .withPrices(1)
            .withEffects(Map.of("100", "2", "200", "3"))
            .build()
    ));
  }

  /**
   * Mock data for an item added on Day 1 that is sold on Day 2, should trigger sold count and
   * revenue calculation
   */
  public static DailySalesParam soldListingsScenario() {

    return new DailySalesParam(List.of(
        ItemMarketEntryBuilder.builder()
            .withItemId(1L)
            .withDate(LocalDate.of(2023, 1, 1))
            .withPrices(1000)
            .withEffects(Map.of("100", "1", "200", "2"))
            .build(),
        ItemMarketEntryBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withItemId(1L)
            .withPrices(2000)
            .withEffects(Map.of("100", "2", "200", "3"))
            .build()

    ));
  }
}
