package com.dofuspulse.api.metrics.fixtures.scenarios;

import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.fixtures.builders.ItemPriceBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CraftCostScenarioFactory {

  public static CraftCostParams multipleIngredientsCraftHistoryScenario() {

    return new CraftCostParams(List.of(
        //ingredients prices for date 1
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withItemId(1L)
            .withPrices(1, 100, 1000, 10000)
            .build(),

        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withItemId(2L)
            .withPrices(50, 500, 5000, 50000)
            .build(),

        //ingredients prices for date 2
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withItemId(1L)
            .withPrices(100, 1000, 10000, 100000)
            .build(),

        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withItemId(2L)
            .withPrices(500, 5000, 50000, 500000)
            .build()

    ), Map.of(1L, 30, 2L, 20));
  }


  public static CraftCostParams missingIngredientPriceScenario() {

    return new CraftCostParams(List.of(
        //ingredients prices for date 1
        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withItemId(1L)
            .withPrices(1, 100, 1000, 10000)
            .build(),

        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 1))
            .withItemId(2L)
            .withPrices(50, 500, 5000, 50000)
            .build(),

//          Missing one ingredient price for date 2
//          ItemPriceBuilder.builder()
//              .withDate(LocalDate.of(2023, 1, 2))
//              .withItemId(1L)
//              .withPrices(100, 1000, 10000, 100000)
//              .build(),

        ItemPriceBuilder.builder()
            .withDate(LocalDate.of(2023, 1, 2))
            .withItemId(2L)
            .withPrices(500, 5000, 50000, 500000)
            .build()

    ), Map.of(1L, 30, 2L, 20));
  }
}
