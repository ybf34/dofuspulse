package com.dofuspulse.api.projections;

import java.util.List;

public record ProfitMarginList(Long itemId, List<ProfitMetrics> profitMetrics, double avgRoi) {
}