package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.projections.ItemMarketEntryProjection;
import java.util.List;

public record DailySalesParam(List<ItemMarketEntryProjection> itemMarketEntries) implements
    BaseParams {
}
