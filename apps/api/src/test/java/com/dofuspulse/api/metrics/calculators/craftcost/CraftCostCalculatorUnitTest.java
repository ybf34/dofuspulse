package com.dofuspulse.api.metrics.calculators.craftcost;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.CraftCostCalculator;
import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.fixtures.scenarios.CraftCostScenarioFactory;
import com.dofuspulse.api.projections.CraftCost;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Craft Cost Calculator Unit Tests")
public class CraftCostCalculatorUnitTest {

  @Test
  @DisplayName("Should calculate and return correct craft cost for each date")
  public void shouldCalculateAndReturnCorrectCraftCostForEachDate() {

    CraftCostCalculator craftCostCalculator = new CraftCostCalculator();
    CraftCostParams craftCostParams = CraftCostScenarioFactory.multipleIngredientsCraftHistoryScenario();

    List<CraftCost> craftCosts = craftCostCalculator.calculate(craftCostParams);

    assertAll(
        () -> assertEquals(2, craftCosts.size()),
        () -> assertEquals(1030, craftCosts.getFirst().craftCost()),
        () -> assertEquals(13000, craftCosts.get(1).craftCost())
    );
  }

  /**
   * Test to ensure that the calculator will not return a craftcost if an ingredient price is
   * missing for a date
   */
  @Test
  @DisplayName("Should Return Empty Craft cost for the concerned date that misses ingredient prices")
  public void shouldReturnEmptyCraftCostIfMissingAnIngredientPrice() {

    CraftCostCalculator craftCostCalculator = new CraftCostCalculator();
    CraftCostParams craftCostParams = CraftCostScenarioFactory.missingIngredientPriceScenario();

    List<CraftCost> craftCosts = craftCostCalculator.calculate(craftCostParams);

    //should return only one CraftCost
    // as one ingredient is missing an ingredient price in the second snapshot date

    assertEquals(1, craftCosts.size());
  }
}
