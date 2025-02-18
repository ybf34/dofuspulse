package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.projections.ItemPrice;
import java.util.List;
import java.util.Map;

public record CraftCostParams(List<ItemPrice> ingredientsPrices,
                              Map<Long, Integer> ingredientQuantityMap) implements BaseParams {}
