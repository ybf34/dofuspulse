package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.PriceHistory;
import java.util.List;

public record ProfitMetricsParams(List<PriceHistory> itemPrices,
                                  List<CraftCost> craftCosts) implements BaseParams {}