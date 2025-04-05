package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ItemPrice;
import java.util.List;

public record ProfitMetricsParams(List<ItemPrice> itemPrices,
                                  List<CraftCost> craftCosts) implements BaseParams {}