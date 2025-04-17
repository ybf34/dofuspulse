package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.model.ItemMarketEntry;
import java.util.List;

public record DailySalesParam(List<ItemMarketEntry> itemMarketEntries) implements BaseParams {
}
