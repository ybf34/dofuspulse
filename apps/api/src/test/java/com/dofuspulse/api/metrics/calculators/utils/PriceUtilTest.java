package com.dofuspulse.api.metrics.calculators.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceUtilTest {

  @Test
  void shouldReturnMinimumUnitPriceAmongAllQuantities() {
    List<Integer> prices = Arrays.asList(0, 1000, 10000, 30000);
    int minPrice = PriceUtil.getMinimumUnitPrice(prices);

    //should return 30, as 30 000 is minimum unit price among all quantities

    assertEquals(30, minPrice);

  }

}