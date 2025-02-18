package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.model.ItemSalesSnapshot;
import java.util.List;

public record DailySalesParam(List<ItemSalesSnapshot> items) implements BaseParams {
}
